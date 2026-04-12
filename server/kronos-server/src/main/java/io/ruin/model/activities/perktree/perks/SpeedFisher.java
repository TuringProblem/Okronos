package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class SpeedFisher extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	@Override
	public void activatePerk() {
		isActive = true;
	}

	public double getFishChance() {
		return (1.0 + (10 + (perkLevel * 5.0))) / 100;
	}

	@Override
	public String getPerkName() {
		return "Speed Fisher";
	}

	@Override
	public String getPerkDescription() {
		return "Fish faster.";
	}

	@Override
	public String getPerkEffect() {
		return "You will catch fish " + getFishChance() * 100 + "% faster.";
	}

	private double getFishChance(int level) {
		return 1 + (10 + (level * 5));
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will catch fish faster than usual.<br><br>" +
			"At level 1 you will catch fish " + getFishChance(1) + "% faster than usual.<br><br>" +
			"At level 2 you will catch fish " + getFishChance(2) + "% faster than usual.<br><br>" +
			"At level 3 you will catch fish " + getFishChance(3) + "% faster than usual.<br><br>" +
			"At level 4 you will catch fish " + getFishChance(4) + "% faster than usual.<br><br>" +
			"At level 5 you will catch fish " + getFishChance(5) + "% faster than usual."
			;
	}
}
