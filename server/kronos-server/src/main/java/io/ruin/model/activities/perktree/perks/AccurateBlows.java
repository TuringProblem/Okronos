package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class AccurateBlows extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public double getAccuracyBoost() {
		return (7.5 + (5 * this.perkLevel)) / 100;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Accurate Blows";
	}

	@Override
	public String getPerkDescription() {
		return "Special attack accuracy";
	}

	@Override
	public String getPerkEffect() {
		return "When using special attacks, your attacks will be " + getAccuracyBoost() * 100 + "% more accurate.";
	}

	private double getAccuracyBoost(int level) {
		return (7.5 + (5 * level));
	}

	@Override
	public String getRepositoryDescription() {
		return "This perk will boost your accuracy when performing special attacks.<br><br>" +
			"At the perk level 1 your special attack accuracy will be boosted by " + getAccuracyBoost(1) + "%.<br><br>" +
			"At the perk level 2 your special attack accuracy will be boosted by " + getAccuracyBoost(2) + "%.<br><br>" +
			"At the perk level 3 your special attack accuracy will be boosted by " + getAccuracyBoost(3) + "%.<br><br>" +
			"At the perk level 4 your special attack accuracy will be boosted by " + getAccuracyBoost(4) + "%.<br><br>" +
			"At the perk level 5 your special attack accuracy will be boosted by " + getAccuracyBoost(5) + "%.";
	}
}
