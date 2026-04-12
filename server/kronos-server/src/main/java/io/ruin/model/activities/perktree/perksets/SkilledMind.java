package io.ruin.model.activities.perktree.perksets;

import io.ruin.model.activities.perktree.PlayerPerkSet;

public class SkilledMind extends PlayerPerkSet {

	public double getChanceToDoubleExperience() {
		return (2.5 + (5.5 * getLevel())) / 100;
	}

	@Override
	public String getPerkSetName() {
		return "Skilled Mind";
	}

	@Override
	public String getPerkSetDescription() {
		return "Experience Boost";
	}

	private double getChanceToDoubleExperience(int level) {
		return 2.5 + (5.5 * level);
	}

	@Override
	public String getPerkSetEffect() {
		return "When this perk set is active you will have a chance to double incoming experience meaning you will get 2x the experience when gaining experience at any time.<br><br>" +
			"At level 1 you will have a " + getChanceToDoubleExperience(1) + "% chance to double your experience.<br><br>" +
			"At level 2 you will have a " + getChanceToDoubleExperience(2) + "% chance to double your experience.<br><br>" +
			"At level 3 you will have a " + getChanceToDoubleExperience(3) + "% chance to double your experience.<br><br>" +
			"At level 4 you will have a " + getChanceToDoubleExperience(4) + "% chance to double your experience.<br><br>" +
			"At level 5 you will have a " + getChanceToDoubleExperience(5) + "% chance to double your experience.<br><br>"
			;
	}
}
