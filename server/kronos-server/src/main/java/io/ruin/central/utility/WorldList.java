package io.ruin.central.utility;

import io.ruin.Server;
import io.ruin.api.buffer.OutBuffer;
import io.ruin.api.process.ProcessWorker;
import io.ruin.model.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class WorldList {

	private static final Logger log = LoggerFactory.getLogger(WorldList.class);

	private static volatile boolean forceUpdate = true;
	private static int updateTicks;

	public static volatile byte[] worldListData = new byte[0];
	private static final ProcessWorker worker = Server.newWorker("world-list", 1000L, 4);

	public static void start() {
		worker.queue(() -> {
			WorldList.update();
			return false;
		});
	}

	public static void shutdown() {
		worker.shutdown();
	}

	public static void add() {
		forceUpdate = true;
	}

	public static void remove() {
		forceUpdate = true;
	}

	private static void update() {
		if (!forceUpdate && ++updateTicks < 10) {
			return;
		}
		forceUpdate = false;
		updateTicks = 0;
		try {
			final int worldsCount = 1;
			final OutBuffer out = new OutBuffer(255);
			out.addInt(0);
			out.addShort(worldsCount);
			{
				out.addShort(World.id);
				out.addInt(World.settings);
				out.addStringNullTerminated(World.address);
				out.addStringNullTerminated(World.name);
				out.addByte(World.flag.ordinal());
				out.addShort(World.playerCount());
			}
			final int pos = out.position();
			out.position(0);
			out.addInt(pos - 4);
			out.position(pos);
			worldListData = out.toByteArray();
		} catch (final Exception e) {
			log.error("Failed to update world list data", e);
		}
	}
}
