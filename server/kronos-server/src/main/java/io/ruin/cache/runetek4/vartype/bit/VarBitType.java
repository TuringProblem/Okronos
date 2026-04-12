package io.ruin.cache.runetek4.vartype.bit;

import io.ruin.Server;
import io.ruin.api.buffer.InBuffer;
import io.ruin.api.filestore.IndexFile;
import io.ruin.cache.runetek4.vartype.player.VarPlayerBit;

import java.util.ArrayList;
import java.util.HashMap;

public class VarBitType {

	public static VarBitType[] LOADED;

	public static void load() {
		IndexFile index = Server.fileStore.get(2);
		LOADED = new VarBitType[index.getLastFileId(14) + 1];
		HashMap<Integer, ArrayList<VarBitType>> varpMap = new HashMap<>();
		int highestVarpId = -1;
		for (int id = 0; id < LOADED.length; id++) {
			byte[] data = index.getFile(14, id);
			VarBitType bit = new VarBitType(id);
			bit.decode(new InBuffer(data));

			ArrayList<VarBitType> bits = varpMap.get(bit.varpId);
			if (bits == null)
				bits = new ArrayList<>();
			bits.add(bit);
			varpMap.put(bit.varpId, bits);

			if (bit.varpId > highestVarpId)
				highestVarpId = bit.varpId;

			LOADED[id] = bit;
		}
		VarPlayerBit.LOADED = new VarPlayerBit[30_000];
		varpMap.forEach((varpId, bits) -> new VarPlayerBit(varpId, bits.toArray(new VarBitType[bits.size()])));
	}

	public static VarBitType get(int id) {
		if (id < 0 || id >= LOADED.length) {
			return null;
		}

		return LOADED[id];
	}

	public int id;
	public int varpId;
	public int leastSigBit;
	public int mostSigBit;

	public VarBitType(int id) {
		this.id = id;
	}

	private void decode(InBuffer in) {
		int opcode;
		while ((opcode = in.readUnsignedByte()) != 0)
			decode(in, opcode);
	}

	private void decode(InBuffer in, int i) {
		if (i == 1) {
			varpId = in.readUnsignedShort();
			leastSigBit = in.readUnsignedByte();
			mostSigBit = in.readUnsignedByte();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + varpId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VarBitType other = (VarBitType) obj;
		if (id != other.id)
			return false;
		if (varpId != other.varpId)
			return false;
		return true;
	}

}
