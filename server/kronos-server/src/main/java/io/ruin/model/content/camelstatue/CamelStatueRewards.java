package io.ruin.model.content.camelstatue;

public enum CamelStatueRewards {
	DOUBLE_REASON_POINTS("Double Reason Points", "When this is active you will receive double the amount of reason points from anything.", 200000000),
	DOUBLE_PERK_EXPERIENCE("Double Perk Experience", "When this is active you will receive double the amount of perk experience for completing tasks.", 450000000),
	DOUBLE_SLAYER_POINTS("Double Slayer Points", "When this is active you will receive double the amount of slayer points from completing slayer tasks.", 600000000),
	BOOSTED_PET_RATES("Boosted Pet Rates", "When this is active you will have a 15% higher chance of obtaining any pet.", 800000000),
	ADDITIONAL_ROLL_CHANCE("Additional Drop Roll Chance", "When this is active you will have a 33% chance to roll an additional drop when killing monsters.", 1000000000),
	DOUBLE_RAID_POINTS("Double Raid Points", "When this is active you will receive double the points in all raids.", 1500000000),
	DROP_RATE_BOOST("Drop Rate Boost", "When this is active you will have an additional 5% Drop rate.", 2000000000),
	DOUBLE_EXPERIENCE("Double Experience", "When this is active your incoming experience will be doubled.", 3000000000L),

	;
	private final long unlockAmount;
	private final String name;
	private final String description;

	CamelStatueRewards(String name, String description, long unlockAmount) {
		this.unlockAmount = unlockAmount;
		this.name = name;
		this.description = description;
	}

	public long getUnlockAmount() {
		return unlockAmount;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
}
