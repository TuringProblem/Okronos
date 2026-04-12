package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class TakeYourTime extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public float getAccuracyBoost() {
		return (perkLevel * 15.0f) / 100;
	}

	@Override
	public void activatePerk() {
		isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Take Your Time";
	}

	@Override
	public String getPerkDescription() {
		return "Attack slower, accuracy boosted.";
	}

	@Override
	public String getPerkEffect() {
		return "You will attack slower, but you will have a " + ((int) (getAccuracyBoost() * 100)) + "% accuracy boost.";
	}

	private double getAccuracyBoost(int level) {
		return (level * 15.0);
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will attack slower but your attacks will be more accurate than the usually are.<br><br>" +
			"At level 1 you will have a boosted accuracy of " + getAccuracyBoost(1) + "%.<br><br>" +
			"At level 2 you will have a boosted accuracy of " + getAccuracyBoost(2) + "%.<br><br>" +
			"At level 3 you will have a boosted accuracy of " + getAccuracyBoost(3) + "%.<br><br>" +
			"At level 4 you will have a boosted accuracy of " + getAccuracyBoost(4) + "%.<br><br>" +
			"At level 5 you will have a boosted accuracy of " + getAccuracyBoost(5) + "%."
			;
	}
}
