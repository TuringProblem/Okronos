
package io.ruin.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.ruin.api.utils.ExecutorUtils;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static io.ruin.util.ByteBufExtensions.readString;
import static io.ruin.util.ByteBufExtensions.writeString;

@Slf4j
public final class HWIDManager implements Runnable {

	private static final Path DEFAULT_PATH = Path.of("data", "banned_hwids.dat");
	private static final ObjectSet<String> banned = new ObjectOpenHashSet<>();
	private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	public static void shutdown() {
		ExecutorUtils.shutdown(executor);
	}

	public static ScheduledExecutorService start() {
		load(DEFAULT_PATH);

		final HWIDManager hwidManager = new HWIDManager();

		executor.scheduleAtFixedRate(hwidManager, 1, 1, TimeUnit.MINUTES);

		return executor;
	}

	public static void load(final Path path) {
		if (Files.notExists(path)) {
			return;
		}

		final byte[] data;
		try {
			data = Files.readAllBytes(path);
		} catch (final IOException e) {
			log.error("Failed to load HWIDs from path " + path, e);
			return;
		}

		final ByteBuf buf = Unpooled.wrappedBuffer(data);
		try {
			final int amount = buf.readInt();
			for (int i = 0; i < amount; i++) {
				final String hwid = readString(buf);
				banned.add(hwid);
			}
		} finally {
			buf.release();
		}
	}

	public static void save(final Path savePath) {
		Path path = savePath;
		if (path == null) {
			path = DEFAULT_PATH;
		}

		final var buf = PooledByteBufAllocator.DEFAULT.buffer();
		try {
			synchronized (banned) {
				final int amount = banned.size();
				buf.writeInt(amount);
				for (final String hwid : banned) {
					writeString(buf, hwid);
				}
			}

			final byte[] data = new byte[buf.readableBytes()];
			buf.readBytes(data);

			Files.write(path, data);
		} catch (final IOException e) {
			log.error("Failed to save HWIDs to file: " + path, e);
		} finally {
			buf.release();
		}
	}

	public static boolean isBanned(final String hwid) {
		if (!isHwidValid(hwid)) {
			return false;
		}

		synchronized (banned) {
			return banned.contains(hwid);
		}
	}

	public static void ban(final String hwid) {
		if (!isHwidValid(hwid)) {
			return;
		}

		synchronized (banned) {
			banned.add(hwid);
		}

		// Kick all players with the banned HWID
		World.getPlayerStream()
				.filter(player -> player.hwid.equals(hwid))
				.forEach(Player::forceLogout);
	}

	public static boolean isHwidValid(String hwid) {
		if (hwid == null || hwid.isEmpty()) {
			return false;
		}
		if (hwid.equals("unknown")) {
			return false;
		}
		return true;
	}

	public static void unban(final String hwid) {
		if (!isHwidValid(hwid)) {
			return;
		}
		synchronized (banned) {
			banned.remove(hwid);
		}
	}

	@Override
	public void run() {
		save(null);
	}

}
