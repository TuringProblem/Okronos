package io.ruin.data;

import io.ruin.Server;
import io.ruin.api.utils.FileUtils;
import io.ruin.api.utils.JsonUtils;
import io.ruin.api.utils.PackageLoader;
import io.ruin.api.utils.ServerWrapper;
import io.ruin.model.entity.player.Player;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.ruin.api.utils.TimeUtils.timeTaskExceptionally;

//If you're wondering why the files in the impl folder are named unconventionally,
//it's so that their file names don't clash or match with actual model file names..
//Example: npc_combat.java and NPCCombat.java
@Slf4j
public abstract class DataFile {

	public abstract String path();

	/**
	 * Loads the
	 *
	 * @param fileName
	 * @param json
	 * @return
	 */
	public Object fromJson(String fileName, String json) {
		throw new UnsupportedOperationException(
				"neither @fromJson(loadedFile, fileName, json) or @fromJson(fileName, json) has been overridden!");
	}

	public Object fromJson(File loadedFile, String fileName, String json) {
		return fromJson(fileName, json);
	}

	public int priority() {
		return Integer.MAX_VALUE;
	}

	public static void load() throws Exception {
		loadUnpacked();
	}

	public static void loadUnpacked() throws Exception {
		/**
		 * Find all files
		 */
		final Set<DataFile> dataFilesSet = new HashSet<>();
		for (final Class<DataFile> c : PackageLoader.loadExtending(
			DataFile.class,
			"io.ruin.data.impl")
		) {
			final DataFile dataFile = c.getDeclaredConstructor().newInstance();
			dataFilesSet.add(dataFile);
		}

		final var dataFiles = new ArrayList<>(dataFilesSet.stream().toList());
		dataFiles.sort(Comparator.comparingInt(DataFile::priority));
		for (final DataFile dataFile : dataFiles) {
			timeTaskExceptionally(() -> {
				load(dataFile);
			}, ms -> log.info("Loading took {}ms for {}", ms, dataFile.path()));
		}
	}

	public static List<PackedFile> load(DataFile dataFile) throws Exception {
		/**
		 * Load unpacked files
		 */
		List<File> unpackedFiles = new ArrayList<>();
		String path = dataFile.path();
		int i = path.indexOf("*");
		if (i != -1) {
			File searchFolder = FileUtils.get(Server.dataFolder.getAbsolutePath() + "/" + path.substring(0, i));
			String searchExtension = path.substring(i + 1, path.length());
			if (!searchFolder.exists()) {
				log.warn("Folder {} was not found!", searchFolder);
				return new ArrayList<>();
			}
			Files.walk(searchFolder.toPath()).forEach(p -> {
				File unpackedFile = p.toAbsolutePath().toFile();
				if (unpackedFile.isDirectory() || !unpackedFile.getName().endsWith(searchExtension))
					return;
				unpackedFiles.add(unpackedFile);
			});
		} else {
			File unpackedFile = FileUtils.get(Server.dataFolder.getAbsolutePath() + "/" + path);
			unpackedFiles.add(unpackedFile);
		}
		unpackedFiles.sort(Comparator.comparing(File::getName));
		/**
		 * Pack files
		 */
		List<PackedFile> packedFiles = new ArrayList<>();
		for (File unpackedFile : unpackedFiles) {
			if (!unpackedFile.exists()) {
				System.err.println("Failed to find data file: " + unpackedFile.getAbsolutePath());
				continue;
			}
			try {
				packedFiles.add(new PackedFile(dataFile, unpackedFile));
			} catch (Throwable t) {
				t.printStackTrace();
				System.exit(1);
				ServerWrapper.logError("DataFile Failure: " + unpackedFile.getAbsolutePath(), t);
			}
		}
		return packedFiles;
	}

	public static void reload(Player player, Class<? extends DataFile> c) {
		String name = c.getSimpleName();
		try {
			player.sendMessage("Reloading data files for class \"" + name + "\" ...");
			List<PackedFile> files = load(c.getDeclaredConstructor().newInstance());
			if (files.size() == 1)
				player.sendMessage("Successfully reloaded 1 data file for class \"" + name + "\" !");
			else
				player.sendMessage("Successfully reloaded " + files.size() + " data files for class \"" + name + "\" !");
		} catch (Exception e) {
			ServerWrapper.logError("Failed to reload data files for class \"" + name + "\" !", e);
		}
	}

	public static final class PackedFile {

		final String loaderClass;

		final String fileName;

		final String json;

		PackedFile(DataFile dataFile, File unpackedFile) throws IOException {
			loaderClass = dataFile.getClass().getName();
			fileName = unpackedFile.getName().replace(".json", "");
			json = JsonUtils.GSON_EXPOSE
					.toJson(dataFile.fromJson(unpackedFile, fileName, JsonUtils.fromFile(unpackedFile)));
		}

		void load() {
			try {
				DataFile dataFile = (DataFile) Class.forName(loaderClass).getDeclaredConstructor().newInstance();
				dataFile.fromJson(fileName, json);
			} catch (Exception e) {
				ServerWrapper.logError("Failed to load packed data file! class=" + loaderClass, e);
			}
		}

	}

	public static int priority(String dataName, int priority) {
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		char c = dataName.toLowerCase().charAt(0);
		int i = alphabet.indexOf(c) + 1;
		return (i * 100000) + priority;
	}

	@Override
	public boolean equals(Object obj) {
		return ((DataFile) obj).path().equals(this.path());
	}

	@Override
	public int hashCode() {
		return this.path().hashCode();
	}
}
