package io.ruin.cache.runetek5.vartype.constants;

public enum VarTransmitLevel {
	UNKNOWN(-1),
	NEVER(0),
	ON_SET_DIFFERENT(1),
	ON_SET_ALWAYS(2);

	int serialID;

	VarTransmitLevel(int id) {
		serialID = id;
	}

	public int getId(int i) {
		return serialID;
	}

	public static VarTransmitLevel getById(int id) {
		for (VarTransmitLevel key : values()) {
			if (id == key.serialID) {
				return key;
			}
		}
		return null;
	}
}
