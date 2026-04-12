package io.ruin.cache;

import java.util.HashMap;
import java.util.Map;
import io.ruin.Server;
import io.ruin.api.buffer.InBuffer;
import io.ruin.api.filestore.IndexFile;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeadbarType {

	public int field1871;
	public int priority;
	public int hidePriority;
	public int tickDurationOffset;
	public int field1876;
	public int tickDuration;
	int frontSpriteID;
	int backSpriteID;
	public int width;
	public int widthPadding;

	private static Map<Integer, HeadbarType> cached = new HashMap();

	public static HeadbarType get(int id) {
		return cached.get(id);
	}

	public static void load() {
		IndexFile index = Server.fileStore.get(2);
		int size = index.getLastFileId(33) + 1;
		log.info("Loading {} NPC definitions...", size);
		cached = new HashMap<>(size);

		for (int id = 0; id < size; id++) {
			byte[] data = index.getFile(33, id);
			if (data != null) {
				var map = new HeadbarType();
				if (data != null) {
					map.decode(new InBuffer(data));
				}
				cached.put(id, map);
			}
		}
	}

	HeadbarType() {
		this.priority = 255;
		this.hidePriority = 255;
		this.tickDurationOffset = -1;
		this.field1876 = 1;
		this.tickDuration = 70;
		this.frontSpriteID = -1;
		this.backSpriteID = -1;
		this.width = 30;
		this.widthPadding = 0;
	}

	void decode(InBuffer in) {
		for (;;) {
			int var3 = in.readUnsignedByte();
			if (var3 == 0)
				break;
			decode(in, var3);
		}
	}

	void decode(InBuffer var1, int var2) {
		if (var2 == 1) {
			var1.readUnsignedShort();
		} else if (var2 == 2) {
			this.priority = var1.readUnsignedByte();
		} else if (var2 == 3) {
			this.hidePriority = var1.readUnsignedByte();
		} else if (var2 == 4) {
			this.tickDurationOffset = 0;
		} else if (var2 == 5) {
			this.tickDuration = var1.readUnsignedShort();
		} else if (var2 == 6) {
			var1.readUnsignedByte();
		} else if (var2 == 7) {
			this.frontSpriteID = var1.gSmart2or4null();
		} else if (var2 == 8) {
			this.backSpriteID = var1.gSmart2or4null();
		} else if (var2 == 11) {
			this.tickDurationOffset = var1.readUnsignedShort();
		} else if (var2 == 14) {
			this.width = var1.readUnsignedByte();
		} else if (var2 == 15) {
			this.widthPadding = var1.readUnsignedByte();
		}

	}
}
