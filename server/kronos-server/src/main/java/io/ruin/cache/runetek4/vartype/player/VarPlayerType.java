package io.ruin.cache.runetek4.vartype.player;

import io.ruin.Server;
import io.ruin.api.buffer.InBuffer;
import io.ruin.api.filestore.IndexFile;
import io.ruin.cache.runetek5.vartype.VarType;

public class VarPlayerType {
	public static VarPlayerType[] LOADED;

	public int id;
	public int clientcode = 0;

	public static void load() {
		IndexFile index = Server.fileStore.get(2);
		int i = index.getLastFileId(16) + 1;
		LOADED = new VarPlayerType[i];
		for (int id = 0; id < LOADED.length; id++) {
			byte[] data = index.getFile(16, id);
			VarPlayerType def = new VarPlayerType();
			def.id = id;
			def.decode(new InBuffer(data));
			LOADED[id] = def;
		}
	}

	public enum VarPlayerTypeEncodingKey {
		CLIENTCODE(5);

		int serialID;

		public int getId(int i) {
			return serialID;
		}

		VarPlayerTypeEncodingKey(int serialID) {
			this.serialID = serialID;
		}

		static VarPlayerTypeEncodingKey getById(int id) {
			for (VarPlayerTypeEncodingKey key : values()) {
				if (id == key.serialID) {
					return key;
				}
			}
			return null;
		}
	}

	public static VarPlayerType get(int id) {
		if (id < 0 || id >= LOADED.length)
			return null;
		return LOADED[id];
	}

	void decode(InBuffer stream) {
		for (; ; ) {
			int i = stream.readUnsignedByte();
			if (i == 0)
				break;
			readValues(stream, i);
		}
	}

	void readValues(InBuffer stream, int i) {
		VarPlayerTypeEncodingKey key = VarPlayerTypeEncodingKey.getById(i);

		if (key == VarPlayerTypeEncodingKey.CLIENTCODE) {
			clientcode = stream.readUnsignedShort();
		}
	}
}
