package io.ruin.api.social;

public enum SocialRank {
	FRIEND,
	RECRUIT,
	CORPORAL,
	SERGEANT,
	LIEUTENANT,
	CAPTAIN,
	GENERAL,
	OWNER,
	ADMIN(127);

	public final int id;

	private SocialRank() {
		this.id = this.ordinal();
	}

	private SocialRank(int id) {
		this.id = id;
	}
	public static final SocialRank[] VALUES = values();

	public static SocialRank get(int ordinal, SocialRank defaultRank) {
		if (ordinal < 0 || ordinal >= VALUES.length) {
			return defaultRank;
		}
		return VALUES[ordinal];
	}
}

