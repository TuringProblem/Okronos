package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class FletchingDreams extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public double getExpBoost() {
		return (5 + (perkLevel * 7.5)) / 100;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Fletching Dreams";
	}

	@Override
	public String getPerkDescription() {
		return "Fletching experience boost.";
	}

	@Override
	public String getPerkEffect() {
		return "You will receive a " + getExpBoost() * 100 + "% experience boost whilst fletching.";
	}

	private double getExperienceBoost(int level) {
		return 5 + (level * 7.5);
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will receive additional experience whilst fletching.<br><br>" +
			"At level 1 you will receive an additional " + getExperienceBoost(1) + "% fletching experience.<br><br>" +
			"At level 2 you will receive an additional " + getExperienceBoost(2) + "% fletching experience.<br><br>" +
			"At level 3 you will receive an additional " + getExperienceBoost(3) + "% fletching experience.<br><br>" +
			"At level 4 you will receive an additional " + getExperienceBoost(4) + "% fletching experience.<br><br>" +
			"At level 5 you will receive an additional " + getExperienceBoost(5) + "% fletching experience."
			;
	}
}
