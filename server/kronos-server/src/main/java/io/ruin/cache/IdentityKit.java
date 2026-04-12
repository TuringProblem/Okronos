package io.ruin.cache;

import io.ruin.Server;
import io.ruin.api.buffer.InBuffer;
import io.ruin.api.filestore.IndexFile;

public class IdentityKit {

	/**
	 * Storage
	 */

	public static IdentityKit[] LOADED;

	public static void register() {
		load();
	}

	public static void load() {
		IndexFile index = Server.fileStore.get(2);
		LOADED = new IdentityKit[index.getLastFileId(3) + 1];
		for (int id = 0; id < LOADED.length; id++) {
			byte[] data = index.getFile(3, id);
			IdentityKit def = new IdentityKit();
			def.id = id;
			def.decode(new InBuffer(data));
			LOADED[id] = def;
		}
	}

	public static IdentityKit get(int id) {
		if (id < 0 || id >= LOADED.length)
			return null;
		return LOADED[id];
	}

	/**
	 * Default
	 */

	public int id;
	public int bodyPartId = -1;
	public boolean nonSelectable = false;
	int[] models;
	short[] recolorToFind;
	short[] recolorToReplace;
	short[] retextureToFind;
	short[] rextureToReplace;
	int[] chatheadModels = { -1, -1, -1, -1, -1 };

	void decode(InBuffer stream) {
		for (;;) {
			int i = stream.readUnsignedByte();
			if (i == 0)
				break;
			readValues(stream, i);
		}
	}

	void readValues(InBuffer stream, int i) {
		if (i == 1)
			bodyPartId = stream.readUnsignedByte();
		else if (i == 2) {
			int i_7_ = stream.readUnsignedByte();
			models = new int[i_7_];
			for (int i_8_ = 0; i_8_ < i_7_; i_8_++)
				models[i_8_] = stream.readUnsignedShort();
		} else if (i == 3)
			nonSelectable = true;
		else if (i == 40) {
			int i_9_ = stream.readUnsignedByte();
			recolorToFind = new short[i_9_];
			recolorToReplace = new short[i_9_];
			for (int i_10_ = 0; i_10_ < i_9_; i_10_++) {
				recolorToFind[i_10_] = (short) stream.readUnsignedShort();
				recolorToReplace[i_10_] = (short) stream.readUnsignedShort();
			}
		} else if (i == 41) {
			int i_11_ = stream.readUnsignedByte();
			retextureToFind = new short[i_11_];
			rextureToReplace = new short[i_11_];
			for (int i_12_ = 0; i_12_ < i_11_; i_12_++) {
				retextureToFind[i_12_] = (short) stream.readUnsignedShort();
				rextureToReplace[i_12_] = (short) stream.readUnsignedShort();
			}
		} else if (i >= 60 && i < 70)
			chatheadModels[i - 60] = stream.readUnsignedShort();
	}

	@Override
	public String toString() {
		return "idk(" + this.id + ")";
	}

}
