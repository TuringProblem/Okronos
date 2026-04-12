package runelite.launcher;

import java.util.Locale;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Set;
import java.util.function.Consumer;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpRequest;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.IllegalComponentStateException;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.GrayFilter;

public class Launcher {
	static File REASON_HOME = new File(System.getProperty("user.home"), ".reason");
	static File CLIENT_FILE = new File(REASON_HOME, "betaclient.jar");
	static File JRE_DIR = new File(REASON_HOME, "jre");

	static String CLIENT_VERSION = "development-latest";
	static String CLIENT_URL = "https://gitlab.com/api/v4/projects/66004513/packages/generic/client/" + CLIENT_VERSION
			+ "/client.jar";
	static String CLIENT_SHA_URL = "https://gitlab.com/api/v4/projects/66004513/packages/generic/client/" + CLIENT_VERSION
			+ "/client.sha256";

	static {
		try {
			Properties properties = new Properties();
			properties.load(Launcher.class.getResourceAsStream("/properties.ini"));
			CLIENT_VERSION = properties.getProperty("client_version");
			CLIENT_URL = "https://gitlab.com/api/v4/projects/66004513/packages/generic/client/" + CLIENT_VERSION
					+ "/client.jar";
			CLIENT_SHA_URL = "https://gitlab.com/api/v4/projects/66004513/packages/generic/client/" + CLIENT_VERSION
					+ "/client.sha256";
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		if (!REASON_HOME.exists()) {
			if (!REASON_HOME.mkdirs()) {
				throw new RuntimeException("Unable to create home directory at " + REASON_HOME.getAbsolutePath());
			}
		}

		if (!JRE_DIR.exists()) {
			if (!JRE_DIR.mkdirs()) {
				throw new RuntimeException("Unable to create home directory at " + REASON_HOME.getAbsolutePath());
			}
		}

		Properties properties = new Properties();
		properties.load(Launcher.class.getResourceAsStream("/properties.ini"));

		SplashScreen.init();

		SplashScreen.stage(0.0, "Loading...", "Checking system.");

		if (!new File(getJavaExe()).exists()) {
			syncFile(jreFilePacked(), javaDownloadLink(), javaDownloadLinkSha256(),
					(p, d, t) -> {
						SplashScreen.stage(0, 1, "Loading...", "Checking for jre update.", (int) d, (int) t, false);
					},
					(p, d, t) -> {
						SplashScreen.stage(0, 1, "Loading...", "Downloading jre.", (int) d, (int) t, true);
					});
			unpackJRE();
		}

		syncFile(CLIENT_FILE,
				CLIENT_URL,
				CLIENT_SHA_URL,
				(p, d, t) -> {
					SplashScreen.stage(0, 1, "Loading...", "Checking for client update.", (int) d, (int) t, false);
				},
				(p, d, t) -> {
					SplashScreen.stage(0, 1, "Loading...", "Downloading client update.", (int) d, (int) t, true);
				});

		SplashScreen.stage(0.0, "Loading...", "Launching client");
		SplashScreen.stop();

		ProcessBuilder builder = new ProcessBuilder(
				getJavaExe(),
				"-Xmx768m",
				"--add-opens=java.desktop/com.apple.eawt=ALL-UNNAMED",
				"--add-opens=java.base/java.lang=ALL-UNNAMED",
				"-jar", CLIENT_FILE.getAbsolutePath());
		builder.inheritIO();
		builder.start();
	}

	static void syncFile(File local, String remoteURL, String remoteURLSha256, ProgressListener update,
			ProgressListener download) throws Exception {
		String shaRemote = new String(download(remoteURLSha256, update)).split(" ")[0];
		String shaLocal = sha256(local);
		if (!shaRemote.equals(shaLocal)) {
			byte[] data = download(remoteURL, download);
			FileOutputStream fos = new FileOutputStream(local);
			fos.write(data);
			fos.close();
		}
	}

	static void unpackJRE() throws Exception {
		switch (OsCheck.getOperatingSystemType()) {
			case Windows:
				extractZip(jreFilePacked(), JRE_DIR);
				break;
			case Linux:
				extractTarGZ(jreFilePacked(), JRE_DIR);
				break;
			case MacOSARM:
				extractTarGZ(jreFilePacked(), JRE_DIR);
				break;
			default:
				throw new RuntimeException();
		}
	}

	public static void extractTarGZ(File file, File out) throws Exception {
		FileInputStream in = new FileInputStream(file);
		int BUFFER_SIZE = 4096;
		GzipCompressorInputStream gzipIn = new GzipCompressorInputStream(in);
		try (TarArchiveInputStream tarIn = new TarArchiveInputStream(gzipIn)) {
			TarArchiveEntry entry;

			while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
				SplashScreen.stage(0, "Loading...", "Unpacking " + entry.getName());
				String entryName = entry.getName().substring(entry.getName().indexOf("/") + 1);
				if (entryName.isEmpty()) {
					continue;
				}
				if (entry.isDirectory()) {
					File f = new File(out, entryName);
					boolean created = f.mkdir();
					if (!created) {
						System.out.printf("Unable to create directory '%s', during extraction of archive contents.\n",
								f.getAbsolutePath());
					}
				} else {
					File f = new File(out, entryName);
					try {
						if (!f.exists()) {
							f.createNewFile();
						}
						FileOutputStream fos = new FileOutputStream(f, false);
						int count;
						byte data[] = new byte[BUFFER_SIZE];
						try (BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER_SIZE)) {
							while ((count = tarIn.read(data, 0, BUFFER_SIZE)) != -1) {
								dest.write(data, 0, count);
							}
						}
						Set<PosixFilePermission> perms = posixFromInt(entry.getMode());
						Files.setPosixFilePermissions(f.toPath(), perms);
						if (perms.contains(PosixFilePermission.OTHERS_EXECUTE)
								|| perms.contains(PosixFilePermission.OWNER_EXECUTE)) {
							f.setExecutable(true);
						}
					} catch (Exception e) {
						System.err.println("Error while extracting: " + f.getAbsolutePath());
						e.printStackTrace();
					}
				}
			}
		}

		SplashScreen.stage(1, "Loading...", "Unpacked.");
	}

	public static void extractZip(File file, File out) throws Exception {
		FileInputStream in = new FileInputStream(file);
		int BUFFER_SIZE = 4096;
		try (ZipArchiveInputStream tarIn = new ZipArchiveInputStream(in)) {
			ZipArchiveEntry entry;

			while ((entry = (ZipArchiveEntry) tarIn.getNextEntry()) != null) {
				SplashScreen.stage(0, "Loading...", "Unpacking " + entry.getName());
				String entryName = entry.getName().substring(entry.getName().indexOf("/") + 1);
				if (entryName.isEmpty()) {
					continue;
				}
				if (entry.isDirectory()) {
					File f = new File(out, entryName);
					boolean created = f.mkdir();
					if (!created) {
						System.out.printf("Unable to create directory '%s', during extraction of archive contents.\n",
								f.getAbsolutePath());
					}
				} else {
					int count;
					byte data[] = new byte[BUFFER_SIZE];
					File f = new File(out, entryName);
					FileOutputStream fos = new FileOutputStream(f, false);
					try (BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER_SIZE)) {
						while ((count = tarIn.read(data, 0, BUFFER_SIZE)) != -1) {
							dest.write(data, 0, count);
						}
					}
				}
			}
		}

		SplashScreen.stage(1, "Loading...", "Unpacked.");
	}

	public static Set<PosixFilePermission> posixFromInt(int perms) {
		final char[] ds = Integer.toString(perms).toCharArray();
		final char[] ss = { '-', '-', '-', '-', '-', '-', '-', '-', '-' };
		for (int i = ds.length - 1; i >= 0; i--) {
			int n = ds[i] - '0';
			if (i == ds.length - 1) {
				if ((n & 1) != 0)
					ss[8] = 'x';
				if ((n & 2) != 0)
					ss[7] = 'w';
				if ((n & 4) != 0)
					ss[6] = 'r';
			} else if (i == ds.length - 2) {
				if ((n & 1) != 0)
					ss[5] = 'x';
				if ((n & 2) != 0)
					ss[4] = 'w';
				if ((n & 4) != 0)
					ss[3] = 'r';
			} else if (i == ds.length - 3) {
				if ((n & 1) != 0)
					ss[2] = 'x';
				if ((n & 2) != 0)
					ss[1] = 'w';
				if ((n & 4) != 0)
					ss[0] = 'r';
			}
		}
		String sperms = new String(ss);
		return PosixFilePermissions.fromString(sperms);
	}

	static File jreFilePacked() {
		switch (OsCheck.getOperatingSystemType()) {
			case Windows:
				return new File(REASON_HOME, "jre.zip");
			case Linux:
				return new File(REASON_HOME, "jre.tar.gz");
			case MacOSARM:
				return new File(REASON_HOME, "jre.tar.gz");
			default:
				throw new RuntimeException();
		}
	}

	static String javaDownloadLink() {
		switch (OsCheck.getOperatingSystemType()) {
			case Windows:
				return "https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.5%2B11/OpenJDK21U-jre_x64_windows_hotspot_21.0.5_11.zip";
			case Linux:
				return "https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.5%2B11/OpenJDK21U-jre_x64_linux_hotspot_21.0.5_11.tar.gz";
			case MacOSARM:
				return "https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.5%2B11/OpenJDK21U-jre_aarch64_mac_hotspot_21.0.5_11.tar.gz";
			default:
				throw new RuntimeException();
		}
	}

	static String javaDownloadLinkSha256() {
		switch (OsCheck.getOperatingSystemType()) {
			case Windows:
				return "https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.5%2B11/OpenJDK21U-jre_x64_windows_hotspot_21.0.5_11.zip.sha256.txt";
			case Linux:
				return "https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.5%2B11/OpenJDK21U-jre_x64_linux_hotspot_21.0.5_11.tar.gz.sha256.txt";
			case MacOSARM:
				return "https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.5%2B11/OpenJDK21U-jre_aarch64_mac_hotspot_21.0.5_11.pkg.sha256.txt";
			default:
				throw new RuntimeException();
		}
	}

	public static String getJavaExe() {
		switch (OsCheck.getOperatingSystemType()) {
			case Windows:
				return new File(new File(JRE_DIR, "bin"), "java.exe").getAbsolutePath();
			case Linux:
				return new File(new File(JRE_DIR, "bin"), "java").getAbsolutePath();
			case MacOSARM:
				return new File(new File(JRE_DIR, "Contents/Home/bin"), "java").getAbsolutePath();
			default:
				throw new RuntimeException();
		}
	}

	static byte[] download(String link) throws Exception {
		return download(link, null);
	}

	static interface ProgressListener {
		void handle(double progress, long done, long total);
	}

	static byte[] download(String link, ProgressListener progress) throws Exception {
		URL url = new URL(link);
		HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
		long completeFileSize = httpConnection.getContentLength();
		java.io.BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
		ByteArrayOutputStream fos = new ByteArrayOutputStream();
		java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);

		byte[] data = new byte[1024];
		long downloadedFileSize = 0;
		int x = 0;
		while ((x = in.read(data, 0, 1024)) >= 0) {
			downloadedFileSize += x;
			final double currentProgress = ((((double) downloadedFileSize) / ((double) completeFileSize)));
			if (progress != null) {
				progress.handle(currentProgress, downloadedFileSize, completeFileSize);
			}
			bout.write(data, 0, x);
		}
		bout.close();
		in.close();
		return fos.toByteArray();
	}

	static String localClientSHA() {
		try {
			byte[] buffer = new byte[8192];
			int count;
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(CLIENT_FILE))) {
				while ((count = bis.read(buffer)) > 0) {
					digest.update(buffer, 0, count);
				}
			}

			byte[] hash = digest.digest();
			String result = new String(Base64.getEncoder().encode(hash));
			return result;
		} catch (Exception e) {
			return "";
		}
	}

	static String sha256(File file) {
		try {
			byte[] buffer = new byte[8192];
			int count;
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
				while ((count = bis.read(buffer)) > 0) {
					digest.update(buffer, 0, count);
				}
			}

			byte[] hash = digest.digest();
			final StringBuilder hexString = new StringBuilder();
			for (int i = 0; i < hash.length; i++) {
				final String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (Exception e) {
			return "";
		}
	}

	public static class SplashScreen extends JFrame implements ActionListener {
		private static final int WIDTH = 200;
		private static final int PAD = 10;

		private static SplashScreen INSTANCE;

		private final JLabel action = new JLabel("Loading");
		private final JProgressBar progress = new JProgressBar();
		private final JLabel subAction = new JLabel();
		private final Timer timer;

		private volatile double overallProgress = 0;
		private volatile String actionText = "Loading";
		private volatile String subActionText = "";
		private volatile String progressText = null;

		private SplashScreen() {
			setTitle("RuneLite Launcher");

			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setUndecorated(true);
			setLayout(null);
			Container pane = getContentPane();
			pane.setBackground(ColorScheme.DARKER_GRAY_COLOR);

			Font font = new Font(Font.DIALOG, Font.PLAIN, 12);

			BufferedImage _logo = ImageUtil.loadImageResource(SplashScreen.class, "/runelite_splash.png");
			java.awt.Image logo = _logo.getScaledInstance(WIDTH, WIDTH, java.awt.Image.SCALE_SMOOTH);
			JLabel logoLabel = new JLabel(new ImageIcon(logo));
			pane.add(logoLabel);
			logoLabel.setBounds(0, 0, WIDTH, WIDTH);

			int y = WIDTH;

			pane.add(action);
			action.setForeground(Color.WHITE);
			action.setBounds(0, y, WIDTH, 16);
			action.setHorizontalAlignment(SwingConstants.CENTER);
			action.setFont(font);
			y += action.getHeight() + PAD;

			pane.add(progress);
			progress.setForeground(ColorScheme.BRAND_LUNEX);
			progress.setBackground(ColorScheme.BRAND_LUNEX.darker().darker());
			progress.setBorder(new EmptyBorder(0, 0, 0, 0));
			progress.setBounds(0, y, WIDTH, 14);
			progress.setFont(font);
			progress.setUI(new BasicProgressBarUI() {
				@Override
				protected Color getSelectionBackground() {
					return Color.BLACK;
				}

				@Override
				protected Color getSelectionForeground() {
					return Color.BLACK;
				}
			});
			y += 12 + PAD;

			pane.add(subAction);
			subAction.setForeground(Color.LIGHT_GRAY);
			subAction.setBounds(0, y, WIDTH, 16);
			subAction.setHorizontalAlignment(SwingConstants.CENTER);
			subAction.setFont(font);
			y += subAction.getHeight() + PAD;

			setSize(WIDTH, y);
			setLocationRelativeTo(null);

			timer = new Timer(100, this);
			timer.setRepeats(true);
			timer.start();

			setVisible(true);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.setText(actionText);
			subAction.setText(subActionText);
			progress.setMaximum(1000);
			progress.setValue((int) (overallProgress * 1000));

			String progressText = this.progressText;
			if (progressText == null) {
				progress.setStringPainted(false);
			} else {
				progress.setStringPainted(true);
				progress.setString(progressText);
			}
		}

		public static boolean isOpen() {
			return INSTANCE != null;
		}

		public static void init() {
			try {
				SwingUtilities.invokeAndWait(() -> {
					if (INSTANCE != null) {
						return;
					}

					try {
						// boolean hasLAF = UIManager.getLookAndFeel() instanceof RuneLiteLAF;
						// if (!hasLAF) {
						// UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
						// }
						INSTANCE = new SplashScreen();
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			} catch (InterruptedException | InvocationTargetException bs) {
				throw new RuntimeException(bs);
			}
		}

		public static void stop() {
			SwingUtilities.invokeLater(() -> {
				if (INSTANCE == null) {
					return;
				}

				INSTANCE.timer.stop();
				// The CLOSE_ALL_WINDOWS quit strategy on MacOS dispatches WINDOW_CLOSING events
				// to each frame
				// from Window.getWindows. However, getWindows uses weak refs and relies on gc
				// to remove windows
				// from its list, causing events to get dispatched to disposed frames. The
				// frames handle the events
				// regardless of being disposed and will run the configured close operation. Set
				// the close operation
				// to DO_NOTHING_ON_CLOSE prior to disposing to prevent this.
				INSTANCE.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				INSTANCE.dispose();
				INSTANCE = null;
			});
		}

		public static void stage(double overallProgress, String actionText, String subActionText) {
			stage(overallProgress, actionText, subActionText, null);
		}

		public static void stage(double startProgress, double endProgress,
				String actionText, String subActionText,
				int done, int total, boolean mib) {
			String progress;
			if (mib) {
				final double MiB = 1024 * 1024;
				final double CEIL = 1.d / 10.d;
				progress = String.format("%.1f / %.1f MiB", done / MiB, (total / MiB) + CEIL);
			} else {
				progress = done + " / " + total;
			}
			stage(startProgress + ((endProgress - startProgress) * done / total), actionText, subActionText, progress);
		}

		public static void stage(double overallProgress, String actionText, String subActionText,
				String progressText) {
			if (INSTANCE != null) {
				INSTANCE.overallProgress = overallProgress;
				if (actionText != null) {
					INSTANCE.actionText = actionText;
				}
				INSTANCE.subActionText = subActionText;
				INSTANCE.progressText = progressText;
			}
		}
	}

	public static class ImageUtil {
		static {
			ImageIO.setUseCache(false);
		}

		/**
		 * Creates a {@link BufferedImage} from an {@link Image}.
		 *
		 * @param image An Image to be converted to a BufferedImage.
		 * @return A BufferedImage instance of the same given image.
		 */
		public static BufferedImage bufferedImageFromImage(final Image image) {
			if (image instanceof BufferedImage) {
				return (BufferedImage) image;
			}

			return toARGB(image);
		}

		/**
		 * Creates an ARGB {@link BufferedImage} from an {@link Image}.
		 */
		public static BufferedImage toARGB(final Image image) {
			if (image instanceof BufferedImage && ((BufferedImage) image).getType() == BufferedImage.TYPE_INT_ARGB) {
				return (BufferedImage) image;
			}

			BufferedImage out = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = out.createGraphics();
			g2d.drawImage(image, 0, 0, null);
			g2d.dispose();
			return out;
		}

		/**
		 * Creates a new image with the same alpha channel, but a constant RGB channel
		 */
		public static BufferedImage recolorImage(Image image, Color rgb) {
			BufferedImage out = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = out.createGraphics();
			g2d.drawImage(image, 0, 0, null);
			g2d.setComposite(AlphaComposite.SrcAtop);
			g2d.setColor(rgb);
			g2d.fillRect(0, 0, out.getWidth(), out.getHeight());
			g2d.dispose();
			return out;
		}

		/**
		 * Offsets an image's luminance by a given value.
		 *
		 * @param rawImg The image to be darkened or brightened.
		 * @param offset A signed 8-bit integer value to brighten or darken the image
		 *               with.
		 *               Values above 0 will brighten, and values below 0 will darken.
		 * @return The given image with its brightness adjusted by the given offset.
		 */
		public static BufferedImage luminanceOffset(final Image rawImg, final int offset) {
			BufferedImage image = toARGB(rawImg);
			final int numComponents = image.getColorModel().getNumComponents();
			final float[] scales = new float[numComponents];
			final float[] offsets = new float[numComponents];

			Arrays.fill(scales, 1f);
			for (int i = 0; i < numComponents; i++) {
				offsets[i] = offset;
			}
			// Set alpha to not offset
			offsets[numComponents - 1] = 0f;

			return offset(image, scales, offsets);
		}

		/**
		 * Changes an images luminance by a scaling factor
		 *
		 * @param rawImg     The image to be darkened or brightened.
		 * @param percentage The ratio to darken or brighten the given image.
		 *                   Values above 1 will brighten, and values below 1 will
		 *                   darken.
		 * @return The given image with its brightness scaled by the given percentage.
		 */
		public static BufferedImage luminanceScale(final Image rawImg, final float percentage) {
			BufferedImage image = toARGB(rawImg);
			final int numComponents = image.getColorModel().getNumComponents();
			final float[] scales = new float[numComponents];
			final float[] offsets = new float[numComponents];

			Arrays.fill(offsets, 0f);
			for (int i = 0; i < numComponents; i++) {
				scales[i] = percentage;
			}
			// Set alpha to not scale
			scales[numComponents - 1] = 1f;

			return offset(image, scales, offsets);
		}

		/**
		 * Offsets an image's alpha component by a given offset.
		 *
		 * @param rawImg The image to be made more or less transparent.
		 * @param offset A signed 8-bit integer value to modify the image's alpha
		 *               component with.
		 *               Values above 0 will increase transparency, and values below 0
		 *               will decrease
		 *               transparency.
		 * @return The given image with its alpha component adjusted by the given
		 *         offset.
		 */
		public static BufferedImage alphaOffset(final Image rawImg, final int offset) {
			BufferedImage image = toARGB(rawImg);
			final int numComponents = image.getColorModel().getNumComponents();
			final float[] scales = new float[numComponents];
			final float[] offsets = new float[numComponents];

			Arrays.fill(scales, 1f);
			Arrays.fill(offsets, 0f);
			offsets[numComponents - 1] = offset;
			return offset(image, scales, offsets);
		}

		/**
		 * Offsets an image's alpha component by a given percentage.
		 *
		 * @param rawImg     The image to be made more or less transparent.
		 * @param percentage The ratio to modify the image's alpha component with.
		 *                   Values above 1 will increase transparency, and values below
		 *                   1 will decrease
		 *                   transparency.
		 * @return The given image with its alpha component scaled by the given
		 *         percentage.
		 */
		public static BufferedImage alphaOffset(final Image rawImg, final float percentage) {
			BufferedImage image = toARGB(rawImg);
			final int numComponents = image.getColorModel().getNumComponents();
			final float[] scales = new float[numComponents];
			final float[] offsets = new float[numComponents];

			Arrays.fill(scales, 1f);
			Arrays.fill(offsets, 0f);
			scales[numComponents - 1] = percentage;
			return offset(image, scales, offsets);
		}

		/**
		 * Creates a grayscale image from the given image.
		 *
		 * @param image The source image to be converted.
		 * @return A copy of the given imnage, with colors converted to grayscale.
		 */
		public static BufferedImage grayscaleImage(final BufferedImage image) {
			final Image grayImage = GrayFilter.createDisabledImage(image);
			return ImageUtil.bufferedImageFromImage(grayImage);
		}

		/**
		 * Re-size a BufferedImage to the given dimensions.
		 *
		 * @param image     the BufferedImage.
		 * @param newWidth  The width to set the BufferedImage to.
		 * @param newHeight The height to set the BufferedImage to.
		 * @return The BufferedImage with the specified dimensions
		 */
		public static BufferedImage resizeImage(final BufferedImage image, final int newWidth, final int newHeight) {
			return resizeImage(image, newWidth, newHeight, false);
		}

		/**
		 * Re-size a BufferedImage to the given dimensions.
		 *
		 * @param image               the BufferedImage.
		 * @param newWidth            The width to set the BufferedImage to.
		 * @param newHeight           The height to set the BufferedImage to.
		 * @param preserveAspectRatio Whether to preserve the original image's aspect
		 *                            ratio. When {@code true}, the image
		 *                            will be scaled to have a maximum of
		 *                            {@code newWidth} width and {@code newHeight}
		 *                            height.
		 * @return The BufferedImage with the specified dimensions
		 */
		public static BufferedImage resizeImage(final BufferedImage image, final int newWidth, final int newHeight,
				final boolean preserveAspectRatio) {
			final Image resized;
			if (preserveAspectRatio) {
				if (image.getWidth() > image.getHeight()) {
					resized = image.getScaledInstance(newWidth, -1, Image.SCALE_SMOOTH);
				} else {
					resized = image.getScaledInstance(-1, newHeight, Image.SCALE_SMOOTH);
				}
			} else {
				resized = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
			}
			return ImageUtil.bufferedImageFromImage(resized);
		}

		/**
		 * Re-size a BufferedImage's canvas to the given dimensions.
		 *
		 * @param image     The image whose canvas should be re-sized.
		 * @param newWidth  The width to set the BufferedImage to.
		 * @param newHeight The height to set the BufferedImage to.
		 * @return The BufferedImage centered within canvas of given dimensions.
		 */
		public static BufferedImage resizeCanvas(final BufferedImage image, final int newWidth, final int newHeight) {
			final BufferedImage dimg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
			final int centeredX = newWidth / 2 - image.getWidth() / 2;
			final int centeredY = newHeight / 2 - image.getHeight() / 2;

			final Graphics2D g2d = dimg.createGraphics();
			g2d.drawImage(image, centeredX, centeredY, null);
			g2d.dispose();
			return dimg;
		}

		/**
		 * Rotates an image around its center by a given number of radians.
		 *
		 * @param image The image to be rotated.
		 * @param theta The number of radians to rotate the image.
		 * @return The given image, rotated by the given theta.
		 */
		public static BufferedImage rotateImage(final BufferedImage image, final double theta) {
			AffineTransform transform = new AffineTransform();
			transform.rotate(theta, image.getWidth() / 2.0, image.getHeight() / 2.0);
			AffineTransformOp transformOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
			return transformOp.filter(image, null);
		}

		/**
		 * Flips an image horizontally and/or vertically.
		 *
		 * @param image      The image to be flipped.
		 * @param horizontal Whether the image should be flipped horizontally.
		 * @param vertical   Whether the image should be flipped vertically.
		 * @return The given image, flipped horizontally and/or vertically.
		 */
		public static BufferedImage flipImage(final BufferedImage image, final boolean horizontal, final boolean vertical) {
			int x = 0;
			int y = 0;
			int w = image.getWidth();
			int h = image.getHeight();

			final BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			final Graphics2D g2d = out.createGraphics();

			if (horizontal) {
				x = w;
				w *= -1;
			}

			if (vertical) {
				y = h;
				h *= -1;
			}

			g2d.drawImage(image, x, y, w, h, null);
			g2d.dispose();

			return out;
		}

		/**
		 * Outlines non-transparent pixels of a BufferedImage with the given color.
		 *
		 * @param image The image to be outlined.
		 * @param color The color to use for the outline.
		 * @return The BufferedImage with its edges outlined with the given color.
		 */
		public static BufferedImage outlineImage(final BufferedImage image, final Color color) {
			return outlineImage(image, color, false);
		}

		/**
		 * Outlines non-transparent pixels of a BufferedImage with the given color.
		 * Optionally outlines
		 * corners in addition to edges.
		 *
		 * @param image          The image to be outlined.
		 * @param color          The color to use for the outline.
		 * @param outlineCorners Whether to draw an outline around corners, or only
		 *                       around edges.
		 * @return The BufferedImage with its edges--and optionally, corners--outlined
		 *         with the given color.
		 */
		public static BufferedImage outlineImage(final BufferedImage image, final Color color,
				final Boolean outlineCorners) {
			final BufferedImage filledImage = fillImage(image, color);
			final BufferedImage outlinedImage = new BufferedImage(image.getWidth(), image.getHeight(),
					BufferedImage.TYPE_INT_ARGB);

			final Graphics2D g2d = outlinedImage.createGraphics();
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					if ((x == 0 && y == 0)
							|| (!outlineCorners && Math.abs(x) + Math.abs(y) != 1)) {
						continue;
					}

					g2d.drawImage(filledImage, x, y, null);
				}
			}
			g2d.drawImage(image, 0, 0, null);
			g2d.dispose();

			return outlinedImage;
		}

		/**
		 * @see #loadImageResource(Class, String)
		 */
		@Deprecated
		public static BufferedImage getResourceStreamFromClass(Class<?> c, String path) {
			return loadImageResource(c, path);
		}

		/**
		 * Reads an image resource from a given path relative to a given class.
		 * This method is primarily shorthand for the synchronization and error handling
		 * required for
		 * loading image resources from the classpath.
		 *
		 * @param c    The class to be referenced for the package path.
		 * @param path The path, relative to the given class.
		 * @return A {@link BufferedImage} of the loaded image resource from the given
		 *         path.
		 */
		public static BufferedImage loadImageResource(final Class<?> c, final String path) {
			try (InputStream in = c.getResourceAsStream(path)) {
				synchronized (ImageIO.class) {
					return ImageIO.read(in);
				}
			} catch (IllegalArgumentException e) {
				final String filePath;

				if (path.startsWith("/")) {
					filePath = path;
				} else {
					filePath = c.getPackage().getName().replace('.', '/') + "/" + path;
				}

				throw new IllegalArgumentException(path, e);
			} catch (IOException e) {
				throw new RuntimeException(path, e);
			}
		}

		/**
		 * Fills all non-transparent pixels of the given image with the given color.
		 *
		 * @param image The image which should have its non-transparent pixels filled.
		 * @param color The color with which to fill pixels.
		 * @return The given image with all non-transparent pixels set to the given
		 *         color.
		 */
		public static BufferedImage fillImage(final BufferedImage image, final Color color) {
			final BufferedImage filledImage = new BufferedImage(image.getWidth(), image.getHeight(),
					BufferedImage.TYPE_INT_ARGB);
			for (int x = 0; x < filledImage.getWidth(); x++) {
				for (int y = 0; y < filledImage.getHeight(); y++) {
					int pixel = image.getRGB(x, y);
					int a = pixel >>> 24;
					if (a == 0) {
						continue;
					}

					filledImage.setRGB(x, y, color.getRGB());
				}
			}
			return filledImage;
		}

		/**
		 * Performs a rescale operation on the image's color components.
		 *
		 * @param image   The image to be adjusted.
		 * @param scales  An array of scale operations to be performed on the image's
		 *                color components.
		 * @param offsets An array of offset operations to be performed on the image's
		 *                color components.
		 * @return The modified image after applying the given adjustments.
		 */
		private static BufferedImage offset(final BufferedImage image, final float[] scales, final float[] offsets) {
			return new RescaleOp(scales, offsets, null).filter(image, null);
		}
	}

	public static class ColorScheme {
		/* The orange color used for the branding's accents */
		public static final Color BRAND_ORANGE = new Color(220, 138, 0);
		/* Lunex branding colors */
		public static final Color BRAND_LUNEX = new Color(189, 0, 229);
		/* Lunex branding transparent ALT: 146, 35, 218 */
		public static final Color BRAND_LUNEX_TRANSPARENT = new Color(123, 35, 218, 120);
		/* The orange color used for the branding's accents, with lowered opacity */
		public static final Color BRAND_ORANGE_TRANSPARENT = new Color(220, 138, 0, 120);

		public static final Color DARKER_GRAY_COLOR = new Color(30, 30, 30);
		public static final Color DARK_GRAY_COLOR = new Color(40, 40, 40);
		public static final Color MEDIUM_GRAY_COLOR = new Color(77, 77, 77);
		public static final Color LIGHT_GRAY_COLOR = new Color(165, 165, 165);

		public static final Color TEXT_COLOR = new Color(198, 198, 198);
		public static final Color CONTROL_COLOR = new Color(30, 30, 30);
		public static final Color BORDER_COLOR = new Color(23, 23, 23);

		public static final Color DARKER_GRAY_HOVER_COLOR = new Color(60, 60, 60);
		public static final Color DARK_GRAY_HOVER_COLOR = new Color(35, 35, 35);

		/*
		 * The color for the green progress bar (used in ge offers, farming tracker,
		 * etc)
		 */
		public static final Color PROGRESS_COMPLETE_COLOR = new Color(55, 240, 70);

		/*
		 * The color for the red progress bar (used in ge offers, farming tracker, etc)
		 */
		public static final Color PROGRESS_ERROR_COLOR = new Color(230, 30, 30);

		/*
		 * The color for the orange progress bar (used in ge offers, farming tracker,
		 * etc)
		 */
		public static final Color PROGRESS_INPROGRESS_COLOR = new Color(230, 150, 30);

		/* The color for the price indicator in the ge search results */
		public static final Color GRAND_EXCHANGE_PRICE = new Color(110, 225, 110);

		/* The color for the high alch indicator in the ge search results */
		public static final Color GRAND_EXCHANGE_ALCH = new Color(240, 207, 123);

		/* The color for the limit indicator in the ge search results */
		public static final Color GRAND_EXCHANGE_LIMIT = new Color(50, 160, 250);

		/* The background color of the scrollbar's track */
		public static final Color SCROLL_TRACK_COLOR = new Color(25, 25, 25);

	}

	public static final class OsCheck {
		/**
		 * types of Operating Systems
		 */
		public enum OSType {
			Windows, MacOS, MacOSARM, Linux
		};

		// cached result of OS detection
		protected static OSType detectedOS;

		/**
		 * detect the operating system from the os.name System property and cache
		 * the result
		 * 
		 * @returns - the operating system detected
		 */
		public static OSType getOperatingSystemType() {
			if (detectedOS == null) {
				String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
				String arch = System.getProperty("os.arch");
				if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
					if (arch.equals("aarch64")) {
						detectedOS = OSType.MacOSARM;
					} else {
						detectedOS = OSType.MacOS;
					}
				} else if (OS.indexOf("win") >= 0) {
					detectedOS = OSType.Windows;
				} else if (OS.indexOf("nux") >= 0) {
					detectedOS = OSType.Linux;
				} else {
					throw new IllegalComponentStateException("Unknown operating system: " + OS + " arch: " + arch);
				}
			}
			return detectedOS;
		}
	}
}
