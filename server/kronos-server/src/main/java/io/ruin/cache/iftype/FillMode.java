package io.ruin.cache.iftype;

public enum FillMode {
	SOLID(0),
	UNKNOWN(1),
	UNKNOWN2(2);
	int id;

	FillMode(int id) {
		this.id = id;
	}

	public static FillMode forId(int id) {
		for (FillMode fillMode : values()) {
			if (fillMode.id == id) {
				return fillMode;
			}
		}
		return SOLID;
	}
}
