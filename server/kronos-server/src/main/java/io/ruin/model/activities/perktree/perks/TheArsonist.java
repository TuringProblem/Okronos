package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class TheArsonist extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	public double getExperienceBoost() {
		return (perkLevel * 13.5) / 100;
	}

	public float getLightSpeedBoost() {
		return (perkLevel * 10.0f) / 100;
	}

	@Override
	public String getPerkName() {
		return "The Arsonist";
	}

	@Override
	public String getPerkDescription() {
		return "Firemaking boost.";
	}

	@Override
	public String getPerkEffect() {
		return "You will light fires " + getLightSpeedBoost() * 100 + "%" +
			" faster and you will receive a " + getExperienceBoost() * 100 + "% extra experience in firemaking.";
	}

	private double getExperienceBoost(int level) {
		return (level * 13.5);
	}

	private double getLightSpeedBoost(int level) {
		return (level * 10);
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will light fires faster and you will also receive more experience in firemaking.<br><br>" +
			"At level 1 you will light fires " + getLightSpeedBoost(1) + "% faster than usual and you will also get an additional " + getExperienceBoost(1) + "% of firemaking experience.<br><br>" +
			"At level 2 you will light fires " + getLightSpeedBoost(2) + "% faster than usual and you will also get an additional " + getExperienceBoost(2) + "% of firemaking experience.<br><br>" +
			"At level 3 you will light fires " + getLightSpeedBoost(3) + "% faster than usual and you will also get an additional " + getExperienceBoost(3) + "% of firemaking experience.<br><br>" +
			"At level 4 you will light fires " + getLightSpeedBoost(4) + "% faster than usual and you will also get an additional " + getExperienceBoost(4) + "% of firemaking experience.<br><br>" +
			"At level 5 you will light fires " + getLightSpeedBoost(5) + "% faster than usual and you will also get an additional " + getExperienceBoost(5) + "% of firemaking experience."
			;
	}
}
