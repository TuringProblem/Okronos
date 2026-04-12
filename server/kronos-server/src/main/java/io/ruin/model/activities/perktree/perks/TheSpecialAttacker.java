package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class TheSpecialAttacker extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public double getSpecialAttackSaveMultiplier() {
		double calc = (15 + (perkLevel * 8.5)) / 100;
		return 1 - calc;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "The Special Attacker";
	}

	@Override
	public String getPerkDescription() {
		return "Special attacks require less energy.";
	}

	@Override
	public String getPerkEffect() {
		return "Your special attacks will require " + getSpecialAttackSaveMultiplier(perkLevel) + "% of the initial required energy cost.";
	}

	private double getSpecialAttackSaveMultiplier(int level) {
		return (15 + (level * 8.5));
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active your special attacks will require less special energy than they usually do.<br><br>" +
			"At level 1 your special attacks will require " + getSpecialAttackSaveMultiplier(1) + "% less than usual.<br><br>" +
			"At level 2 your special attacks will require " + getSpecialAttackSaveMultiplier(2) + "% less than usual.<br><br>" +
			"At level 3 your special attacks will require " + getSpecialAttackSaveMultiplier(3) + "% less than usual.<br><br>" +
			"At level 4 your special attacks will require " + getSpecialAttackSaveMultiplier(4) + "% less than usual.<br><br>" +
			"At level 5 your special attacks will require " + getSpecialAttackSaveMultiplier(5) + "% less than usual."
			;
	}
}
