package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class ExperienceEnhancer extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public double getExperienceBoost() {
		return (6.5 * perkLevel) / 100;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Experience Enhancer";
	}

	@Override
	public String getPerkDescription() {
		return "Extra experience boost";
	}

	@Override
	public String getPerkEffect() {
		return "You will receive a " + getExperienceBoost() * 100 + "% extra experience.";
	}

	private double getExperienceBoost(int level) {
		return 6.5 * level;
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will receive bonus experience.<br><br>" +
			"At level 1 you will receive an additional " + getExperienceBoost(1) + "% of experience in any skill.<br><br>" +
			"At level 2 you will receive an additional " + getExperienceBoost(2) + "% of experience in any skill.<br><br>" +
			"At level 3 you will receive an additional " + getExperienceBoost(3) + "% of experience in any skill.<br><br>" +
			"At level 4 you will receive an additional " + getExperienceBoost(4) + "% of experience in any skill.<br><br>" +
			"At level 5 you will receive an additional " + getExperienceBoost(5) + "% of experience in any skill."
			;
	}
}
