
/*
 * Copyright (c) 2016-2017, Adam <Adam@sigterm.info>
 * Copyright (c) 2018, Tomas Slusny <slusnucky@gmail.com>
 * Copyright (c) 2019 Abex
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.rs;

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteStreams;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.RuneLite;
import net.runelite.client.RuneLiteProperties;
import net.runelite.client.RuntimeConfig;
import net.runelite.client.RuntimeConfigLoader;
import net.runelite.client.ui.SplashScreen;
import net.runelite.client.util.CountingInputStream;
import net.runelite.client.util.VerificationException;
import net.runelite.http.api.worlds.World;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.annotation.Nonnull;
import java.applet.Applet;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.function.Supplier;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

import static net.runelite.client.rs.ClientUpdateCheckMode.AUTO;
import static net.runelite.client.rs.ClientUpdateCheckMode.VANILLA;

@Slf4j
@SuppressWarnings({ "deprecation", "removal" })
public class ReasonClientLoader implements Supplier<Client> {
	private static final boolean INJECTED_FROM_DELTA = false;
	private static final int NUM_ATTEMPTS = 6;
	private static File LOCK_FILE = new File(RuneLite.CACHE_DIR, "cache.lock");
	private static File VANILLA_CACHE = new File(RuneLite.CACHE_DIR, "vanilla.cache");
	private static File PATCHED_CACHE = new File(RuneLite.CACHE_DIR, "patched.cache");

	private final OkHttpClient okHttpClient;
	private final ReasonClientConfigLoader clientConfigLoader;
	private ClientUpdateCheckMode updateCheckMode;
	private final WorldSupplier worldSupplier;
	private final RuntimeConfigLoader runtimeConfigLoader;
	private final String javConfigUrl;

	private Object client;

	public ReasonClientLoader(OkHttpClient okHttpClient, ClientUpdateCheckMode updateCheckMode,
			RuntimeConfigLoader runtimeConfigLoader, String javConfigUrl) {
		this.okHttpClient = okHttpClient;
		this.clientConfigLoader = new ReasonClientConfigLoader();
		this.updateCheckMode = updateCheckMode;
		this.worldSupplier = new WorldSupplier(okHttpClient);
		this.runtimeConfigLoader = runtimeConfigLoader;
		this.javConfigUrl = javConfigUrl;
	}

	@Override
	public synchronized Client get() {
		if (client == null) {
			client = doLoad();
		}

		if (client instanceof Throwable) {
			throw new RuntimeException((Throwable) client);
		}
		return (Client) client;
	}

	private Object doLoad() {
		Applet rs = null;
		try {
			SplashScreen.stage(.40, null, "Loading client");

			File oprsInjected = new File(System.getProperty("user.home") + "/.reason/.runelite/cache/injected-client.jar");

			writeInjectedClient(oprsInjected);

			File jarFile = updateCheckMode == AUTO ? oprsInjected : VANILLA_CACHE;
			// create the classloader for the jar while we hold the lock, and eagerly load
			// and link all classes
			// in the jar. Otherwise the jar can change on disk and can break future
			// classloads.
			var config = downloadConfig();
			var classLoader = createJarClassLoader(jarFile);

			SplashScreen.stage(.465, "Starting", "Starting Old School RuneScape");
			rs = loadClient(config, classLoader);
			SplashScreen.stage(.5, null, "Starting core classes");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return rs;
	}

	private RSConfig downloadConfig() throws IOException {
		return new ReasonClientConfigLoader().fetch();
	}

	private void writeInjectedClient(File cachedInjected) throws IOException {
		byte[] currentInjected = ByteStreams.toByteArray(ReasonClientLoader.class.getResourceAsStream("/runelite-injected.bin"));

		cachedInjected.getParentFile().mkdirs();
		Files.write(cachedInjected.toPath(), currentInjected);
	}

	private ClassLoader createJarClassLoader(File jar) throws IOException, ClassNotFoundException {
		try (JarFile jarFile = new JarFile(jar)) {
			ClassLoader classLoader = new ClassLoader(ReasonClientLoader.class.getClassLoader()) {
				@Override
				protected Class<?> findClass(String name) throws ClassNotFoundException {
					String entryName = name.replace('.', '/').concat(".class");
					JarEntry jarEntry;

					try {
						jarEntry = jarFile.getJarEntry(entryName);
					} catch (IllegalStateException ex) {
						throw new ClassNotFoundException(name, ex);
					}

					if (jarEntry == null) {
						throw new ClassNotFoundException(name);
					}

					try {
						InputStream inputStream = jarFile.getInputStream(jarEntry);
						if (inputStream == null) {
							throw new ClassNotFoundException(name);
						}

						byte[] bytes = ByteStreams.toByteArray(inputStream);
						return defineClass(name, bytes, 0, bytes.length);
					} catch (IOException e) {
						throw new ClassNotFoundException(null, e);
					}
				}
			};

			// load all of the classes in this jar; after the jar is closed the classloader
			// will no longer be able to look up classes
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry jarEntry = entries.nextElement();
				String name = jarEntry.getName();
				if (name.endsWith(".class")) {
					name = name.substring(0, name.length() - 6);
					classLoader.loadClass(name.replace('/', '.'));
				}
			}
			return classLoader;
		}
	}

	private Applet loadClient(RSConfig config, ClassLoader classLoader)
			throws ClassNotFoundException, IllegalAccessException, Exception {
		String initialClass = config.getInitialClass();
		Class<?> clientClass = classLoader.loadClass(initialClass);
		Applet rs = (Applet) clientClass.newInstance();
		rs.setStub(new RSAppletStub(config, runtimeConfigLoader));
		return rs;
	}

	private static Certificate[] loadCertificateChain(String name) {
		try (InputStream in = ReasonClientLoader.class.getResourceAsStream(name)) {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
			return certificates.toArray(new Certificate[0]);
		} catch (CertificateException | IOException e) {
			throw new RuntimeException("Unable to parse pinned certificates", e);
		}
	}

	private void verifyJarEntry(JarEntry je, Certificate[][] chains) throws VerificationException {
		if (je.getName().equals("META-INF/JAGEXLTD.SF") || je.getName().equals("META-INF/JAGEXLTD.RSA")) {
			// You can't sign the signing files
			return;
		}

		// Jar entry must match one of the trusted certificate chains
		Certificate[] entryCertificates = je.getCertificates();
		for (Certificate[] chain : chains) {
			if (Arrays.equals(entryCertificates, chain)) {
				return;
			}
		}

		throw new VerificationException("Unable to verify jar entry: " + je.getName());
	}

	private void verifyWholeJar(JarInputStream jis, Certificate[][] chains) throws IOException, VerificationException {
		for (JarEntry je; (je = jis.getNextJarEntry()) != null;) {
			jis.skip(Long.MAX_VALUE);
			verifyJarEntry(je, chains);
		}
	}

	private static class OutageException extends RuntimeException {
		private OutageException(Throwable cause) {
			super(cause);
		}
	}

	private boolean checkOutages() {
		RuntimeConfig rtc = runtimeConfigLoader.tryGet();
		if (rtc != null) {
			return rtc.showOutageMessage();
		}
		return false;
	}
}
