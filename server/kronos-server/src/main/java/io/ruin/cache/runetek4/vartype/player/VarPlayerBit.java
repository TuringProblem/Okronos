package io.ruin.cache.runetek4.vartype.player;

import io.ruin.cache.runetek4.vartype.bit.VarBitType;

public class VarPlayerBit {

	public static VarPlayerBit[] LOADED;

	public static VarPlayerBit get(int id) {
		if (id < 0 || id >= LOADED.length)
			return null;
		return LOADED[id];
	}

	/**
	 * Separator
	 */

	public final int baseVarp;

	public final VarBitType[] bits;

	public VarPlayerBit(int baseVarp, VarBitType[] bits) {
		this.baseVarp = baseVarp;
		this.bits = bits;
		LOADED[baseVarp] = this;
	}

}
