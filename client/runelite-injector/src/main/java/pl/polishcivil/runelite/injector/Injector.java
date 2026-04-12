package pl.polishcivil.runelite.injector;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.net.URLClassLoader;
import java.nio.file.Files;
import java.net.URL;

import pl.polishcivil.runelite.injector.inject.hwid.HWIDInject;

/**
 * Injector
 */
public class Injector {
	private static final File FILE_OUTPUT = new File("lib/runelite-injected.bin");
	private static final File FILE_RUNELITE_ORIGINAL = new File("lib/runelite-injected-original.bin");

	public static void main(String[] args) throws Exception {
		var runeliteInjectedData = applyDelta();
		inject(runeliteInjectedData);
	}

	public static File inject(byte[] original) throws Exception {
		var jarFile = JarFile.fromData(original);
		var classGroup = ClassGroup.fromJar(jarFile);

		Host.apply(classGroup);
		Varp.apply(classGroup);
		RSA.apply(classGroup);
		ClanChatCount.apply(classGroup);
		HWIDInject.apply(classGroup);
		UsernameInject.apply(classGroup);

		var loader = new URLClassLoader(new URL[] { FILE_RUNELITE_ORIGINAL.toURI().toURL() });
		for (var node : classGroup.nodes.entrySet()) {
			var writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES) {
				@Override
				protected String getCommonSuperClass(String type1, String type2) {
					try {
						return super.getCommonSuperClass(type1, type2);
					} catch (Exception e) {
						return Injector.getCommonSuperClass(type1, type2, loader);
					}
				}
			};
			node.getValue().accept(writer);
			jarFile.files.put(node.getKey(), writer.toByteArray());
		}
		jarFile.save(FILE_OUTPUT);
		return FILE_OUTPUT;
	}

	@SuppressWarnings("all")
	static String getCommonSuperClass(String type1, String type2, ClassLoader loader) {
		ClassLoader classLoader = loader;
		Class c;
		Class d;
		try {
			c = Class.forName(type1.replace('/', '.'), false, classLoader);
			d = Class.forName(type2.replace('/', '.'), false, classLoader);
		} catch (Exception var7) {
			throw new RuntimeException(var7.toString());
		}

		if (c.isAssignableFrom(d)) {
			return type1;
		} else if (d.isAssignableFrom(c)) {
			return type2;
		} else if (!c.isInterface() && !d.isInterface()) {
			do {
				c = c.getSuperclass();
			} while (!c.isAssignableFrom(d));

			return c.getName().replace('.', '/');
		} else {
			return "java/lang/Object";
		}
	}

	static byte[] applyDelta() throws Exception {
		return Files.readAllBytes(FILE_RUNELITE_ORIGINAL.toPath());
	}

	public static class ClassGroup {
		public final HashMap<String, ClassNode> nodes = new HashMap<>();

		public static ClassGroup fromJar(JarFile file) {
			var result = new ClassGroup();
			for (var entry : file.files.entrySet()) {
				if (isJagexClass(entry.getKey())) {
					var node = new ClassNode();
					var reader = new ClassReader(entry.getValue());
					reader.accept(node, 0);
					result.nodes.put(entry.getKey(), node);
				}
			}
			return result;
		}

		public ClassNode node(String name) {
			return this.nodes.values().stream().filter(it -> it.name.equals(name)).findFirst().orElse(null);
		}

		public void add(ClassNode node) {
			this.nodes.put(node.name + ".class", node);
		}

		static boolean isJagexClass(String entry) {
			if (entry.equals("client.class")) {
				return true;
			}
			if (entry.length() <= 3 + 6 && !entry.contains("/") && entry.endsWith(".class")) {
				return true;
			}
			return false;
		}
	}

	public static ClassNode read(byte[] data) {
		var node = new ClassNode();
		var reader = new ClassReader(data);
		reader.accept(node, 0);
		node.version = Opcodes.V11;
		return node;
	}

	public static boolean hasLdc(MethodNode node, Object value) {
		return hasLdc(node.instructions, value);
	}

	public static boolean hasLdc(InsnList list, Object value) {
		return findLdc(list, value) != null;
	}

	public static LdcInsnNode findLdc(InsnList list, Object value) {
		for (var ins : list) {
			if (ins instanceof LdcInsnNode ldc) {
				if (ldc.cst.equals(value)) {
					return ldc;
				}
			}
		}
		return null;
	}

	static class JarFile {
		private HashMap<String, byte[]> files = new HashMap<>();

		public static JarFile fromData(byte[] data) throws Exception {
			try (var zipIs = new ZipInputStream(new ByteArrayInputStream(data))) {
				var result = new JarFile();
				ZipEntry entry;
				while ((entry = zipIs.getNextEntry()) != null) {
					var entryData = zipIs.readAllBytes();
					result.files.put(entry.getName(), entryData);
				}
				return result;
			}
		}

		public byte[] entryDataByName(String name) {
			for (var entry : this.files.entrySet()) {
				if (entry.getKey().equals(name)) {
					return entry.getValue();
				}
			}
			return null;
		}

		public static JarFile fromFile(File file) throws Exception {
			try (var zip = new ZipFile(file)) {
				var result = new JarFile();
				var zipEntries = zip.entries();
				while (zipEntries.hasMoreElements()) {
					var entry = zipEntries.nextElement();
					var entryIs = zip.getInputStream(entry);
					var entryData = entryIs.readAllBytes();
					result.files.put(entry.getName(), entryData);
				}
				return result;
			}
		}

		public void save(File outputFile) throws Exception {
			try (var output = new ZipOutputStream(new FileOutputStream(outputFile))) {
				for (var entry : this.files.entrySet()) {
					var zipEntry = entry.getKey();
					var zipData = entry.getValue();
					output.putNextEntry(new ZipEntry(zipEntry));
					output.write(zipData);
					output.closeEntry();
				}
				output.flush();
				output.close();
			}
		}
	}
}
