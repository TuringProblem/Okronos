package io.ruin.central.utility;

import io.ruin.Server;
import io.ruin.api.protocol.Response;
import io.ruin.api.utils.*;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerGroup;
import io.ruin.model.entity.player.PlayerLogin;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
public class CentralSaves {
	private static final Logger logger = Logger.getLogger(CentralSaves.class.getName());

	/**
	 * Normalizes a username for storage and comparison
	 *
	 * @param username The username to normalize
	 * @return The normalized username (lowercase, trimmed)
	 */
	public static String normalizeUsername(String username) {
		if (username == null) return null;
		return username.toLowerCase().trim();
	}

	/**
	 * Saves a player's data with JSON validation
	 */
	public static boolean save(String username, String json) {
		try {
			File file = getSaveFile(normalizeUsername(username));
			Files.write(file.toPath(), json.getBytes(StandardCharsets.UTF_8));
			return true;
		} catch (IOException e) {
			logger.severe("Failed to save player data for " + username + ": " + e.getMessage());
			e.printStackTrace();

			// Fallback save attempt
			try {
				File file = getSaveFile(normalizeUsername(username));
				Files.write(file.toPath(), json.getBytes(StandardCharsets.UTF_8));
				return true;
			} catch (IOException e2) {
				logger.severe("Final save attempt also failed for " + username + ": " + e2.getMessage());
				return false;
			}
		}
	}

	private static File getSaveFile(String normalizedUsername) throws IOException {
		String stage = World.stage.name().toLowerCase();
		String type = World.type.name().toLowerCase();

		Path folder = Paths.get(ServerWrapper.dataFolder.getAbsolutePath(), "runtime", "saves", "players", stage, type);
		Files.createDirectories(folder);

		return folder.resolve(normalizedUsername + ".json").toFile();
	}

	public static boolean characterFileExists(String username) {
		try {
			File file = getSaveFile(normalizeUsername(username));
			return file.exists();
		} catch (IOException e) {
			Server.logError("Error checking character file: " + e.getMessage());
			return false;
		}
	}

	public static boolean deleteCharacterFile(String username) {
		try {
			File file = getSaveFile(normalizeUsername(username));
			if (file.exists()) {
				return file.delete();
			}
			return false;
		} catch (IOException e) {
			Server.logError("Error deleting character file: " + e.getMessage());
			return false;
		}
	}

	public static void normalizeFilenamesInDirectory() throws IOException {
		String stage = World.stage.name().toLowerCase();
		String type = World.type.name().toLowerCase();
		Path directory = Paths.get(ServerWrapper.dataFolder.getAbsolutePath(), "runtime", "saves", "players", stage, type);
		Files.createDirectories(directory);

		if (!Files.isDirectory(directory)) {
			throw new IOException("Invalid directory: " + directory.toAbsolutePath());
		}

		try {
			Files.list(directory).forEach(path -> {
				if (Files.isRegularFile(path)) {
					String filename = path.getFileName().toString();
					String lowercaseFilename = filename.toLowerCase();

					if (!filename.equals(lowercaseFilename)) {
						try {
							Path newPath = path.resolveSibling(lowercaseFilename);
							Files.move(path, newPath, StandardCopyOption.ATOMIC_MOVE);
							logger.info("Normalized filename: " + filename + " -> " + lowercaseFilename);
						} catch (IOException e) {
							logger.warning("Failed to rename file " + filename + ": " + e.getMessage());
						}
					}
				}
			});
		} catch (IOException e) {
			logger.severe("Error while normalizing filenames: " + e.getMessage());
			throw e;
		}
	}
}
