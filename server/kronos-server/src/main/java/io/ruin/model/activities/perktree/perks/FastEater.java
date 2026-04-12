package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class FastEater extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public double getTickMultiplier() {
		return (15 + (12.5 * perkLevel)) / 100;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Fast Eater";
	}

	@Override
	public String getPerkDescription() {
		return "Eat food faster";
	}

	@Override
	public String getPerkEffect() {
		return "The delay between eating again is " + ((int) (getTickMultiplier() * 100)) + "% faster.";
	}

	private double getTickMultiplier(int level) {
		return 15 + (12.5 * level);
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active the delay between eating foor is lower than usual.<br><br>" +
			"At level 1 the delay will be reduced by " + getTickMultiplier(1) + "%.<br><br>" +
			"At level 2 the delay will be reduced by " + getTickMultiplier(2) + "%.<br><br>" +
			"At level 3 the delay will be reduced by " + getTickMultiplier(3) + "%.<br><br>" +
			"At level 4 the delay will be reduced by " + getTickMultiplier(4) + "%.<br><br>" +
			"At level 5 the delay will be reduced by " + getTickMultiplier(5) + "%."
			;
	}
}
