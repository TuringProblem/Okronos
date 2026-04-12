package io.ruin.db;

import io.ruin.api.utils.ServerWrapper;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import lombok.extern.slf4j.Slf4j;
import player.attributes.PlayerAttributeCodec;
import player.attributes.PlayerAttributesRegistry;

import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public final class PlayerDatabase {

	private static final Path DB_ROOT = Path.of(
		ServerWrapper.dataFolder.getAbsolutePath(),
		"runtime", "saves", "players",
		World.stage.name().toLowerCase(),
		World.type.name().toLowerCase()
	);

	private static final Database<Player, String> db = new DatabaseFile<>(
		Player.class,
		PlayerDatabase::saveFile
	);

	public static void register() {
		try {
			Files.createDirectories(DB_ROOT);
		} catch (final Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public static void remove(Player player) {
		db().remove(player.uuid());
	}

	public static boolean insertQueue(Player player) {
		final String uuid = player.uuid();
		final int index = player.getIndex();
		final var attributes = PlayerAttributesRegistry.snapshot(index);

		return db().insertQueue(uuid, player, () -> {
			for (final var attrib : attributes) {
				try {
					final Object value = attrib.value();
					final PlayerAttributeCodec.SaveContext saveContext =
						new PlayerAttributeCodec.SaveContext(uuid);
					attrib.codec().saveRaw(saveContext, value);
				} catch (final Exception e) {
					log.error("Failed to save attributes: ", e);
				}
			}
		});
	}

	public static Database<Player, String> db() {
		return db;
	}

	private static Path saveFile(final String uuid) {
		return DB_ROOT.resolve(uuid + ".json");
	}

}
