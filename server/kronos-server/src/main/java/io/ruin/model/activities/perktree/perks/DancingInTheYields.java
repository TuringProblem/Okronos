package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class DancingInTheYields extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public double getYieldBonus() {
		return (7.5 + (5 * perkLevel)) / 100;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Dancing In The Yields";
	}

	@Override
	public String getPerkDescription() {
		return "Extra yields farming";
	}

	@Override
	public String getPerkEffect() {
		return "You will receive " + ((int) (getYieldBonus() * 100)) + "% extra yield whilst farming.";
	}


	private double getYieldBoost(int level) {
		return 7.5 + (5 * level);
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will receive more yield when harvesting your crops.<br><br>" +
			"At level 1 you will receive " + getYieldBoost(1) + "% extra yeild when harvesting.<br><br>" +
			"At level 2 you will receive " + getYieldBoost(2) + "% extra yeild when harvesting.<br><br>" +
			"At level 3 you will receive " + getYieldBoost(3) + "% extra yeild when harvesting.<br><br>" +
			"At level 4 you will receive " + getYieldBoost(4) + "% extra yeild when harvesting.<br><br>" +
			"At level 5 you will receive " + getYieldBoost(5) + "% extra yeild when harvesting."
			;
	}
}
