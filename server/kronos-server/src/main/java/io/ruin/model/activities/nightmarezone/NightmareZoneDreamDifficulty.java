package io.ruin.model.activities.nightmarezone;

public enum NightmareZoneDreamDifficulty {
	NORMAL(50000), HARD(100000);

	private final int cost;

	NightmareZoneDreamDifficulty(int cost) {
		this.cost = cost;
	}

	public int getCost() {
		return cost;
	}
}
