package io.ruin.utility;

import io.ruin.api.protocol.world.WorldStage;
import io.ruin.api.utils.ExecutorUtils;
import io.ruin.api.utils.ServerWrapper;
import io.ruin.model.World;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author ReverendDread on 6/27/2020 https://www.rune-server.ee/members/reverenddread/
 * @project Kronos
 */
@Slf4j
public class CharacterBackups {

	private static final String BACKUP_PATH = Paths.get(ServerWrapper.dataFolder.getAbsolutePath(), "backups").toString();
	private static final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor((r) -> {
		var thread = new Thread(r, "character-backup");
		return thread;
	});

	private static final long BACKUP_PERIOD = TimeUnit.HOURS.toMillis(1);
	private static final int BUFFER_SIZE = 4096;
	private static String CHARACTER_SAVES_PATH;
	public static boolean enabled = true;

	public static void shutdown() {
		ExecutorUtils.shutdown(service);
	}

	public void start() {
		if (World.stage == WorldStage.LIVE) {
			CHARACTER_SAVES_PATH = Paths.get(ServerWrapper.dataFolder.getAbsolutePath(),
					"runtime", "saves", "players",
					World.stage.name().toLowerCase(),
					World.type.name().toLowerCase()).toString();

			service.scheduleWithFixedDelay(this::backup, BACKUP_PERIOD, BACKUP_PERIOD, TimeUnit.MILLISECONDS);
		}
	}

	@SneakyThrows
	public void backup() {
		try {
			if (!enabled) {
				return;
			}
			log.info("Performing character backup...");

			// Ensure backup directory exists
			Path backupDir = Paths.get(BACKUP_PATH);
			if (Files.notExists(backupDir)) {
				Files.createDirectories(backupDir);
			}

			// Create backup file
			String timestamp = DateFormatUtils.format(System.currentTimeMillis(), "MM-dd-yyyy HH-mm-ss");
			File backupFile = new File(backupDir.toFile(), "players-" + timestamp + ".zip");

			try (FileOutputStream fos = new FileOutputStream(backupFile);
					ZipOutputStream zos = new ZipOutputStream(fos)) {

				File charactersDir = new File(CHARACTER_SAVES_PATH);
				Arrays.stream(charactersDir.listFiles((dir, name) -> name.endsWith(".json")))
						.forEach(it -> {
							try {
								var data = Files.readAllBytes(it.toPath());
								writeSaveToZip(it.getName(), data, zos);
							} catch (Exception e) {
								log.error("Unable to write char backup: ", it, e);
							}
						});

				log.info("Successfully backed up character file(s).");
			}

		} catch (IOException e) {
			log.error("An error occurred during the backup process.", e);
		}
	}

	static record PlayerFileEntry(String name, byte[] bytes) {
	}

	private void writeSaveToZip(String name, byte[] bytes, ZipOutputStream zos) throws IOException {
		ZipEntry zipEntry = new ZipEntry(name);
		zos.putNextEntry(zipEntry);
		try {
			zos.write(bytes);
		} finally {
			zos.closeEntry();
		}
	}
}
