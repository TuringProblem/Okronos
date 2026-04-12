package io.ruin.model.activities.perktree.perks;

import io.ruin.api.utils.Random;
import io.ruin.model.activities.perktree.PlayerPerk;

public class TakingItOnTheChin extends PlayerPerk {
	/*
	More chins when hunting and boosted damage and accuracy chinning
	 */
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	/*
	Chins only
	 */
	public double getExperienceBoost() {
		double calc = (2.5 + (perkLevel * 3)) / 100;
		return 1 + calc;
	}

	public int getChinsToCapture() {
		if (perkLevel == 1) {
			if (Random.rollPercent(50)) return 1;
			else return 2;
		}
		if (perkLevel == 2)
			return 2;
		else
			return 3;
	}


	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Taking It On The Chin";
	}

	@Override
	public String getPerkDescription() {
		return "Hunting chinchompas boost.";
	}

	@Override
	public String getPerkEffect() {
		return "You will catch " + getChinsToCapture() + " instead of 1 and you will receive a "
			+ ((int) (getExperienceBoost() * 100) - 100) + "% experience boost when hunting chinchompas.";
	}

	private double getExperienceBoost(int level) {
		return 2.5 + (level * 3);
	}

	private int getChinsToCapture(int level) {
		if (level == 1) {
			if (Random.rollPercent(50)) return 1;
			else return 2;
		}
		if (perkLevel == 2)
			return 2;
		else
			return 3;
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will catch more chins than usual and you will also have boosted hunter experience when catching chins.<br><br>" +
			"At level 1 you will have a 50:50 chance to catch 1 or 2 chinchompas when hunting chinchompas you will get an additional " + getExperienceBoost(1) + "% more experience.<br><br>" +
			"At level 2 you will receive " + getChinsToCapture(2) + " chinchompas a capture and you will get an additional " + getExperienceBoost(2) + "% more experience.<br><br>" +
			"At level 3 you will receive " + getChinsToCapture(3) + " chinchompas a capture and you will get an additional " + getExperienceBoost(3) + "% more experience.<br><br>" +
			"At level 4 you will receive " + getChinsToCapture(4) + " chinchompas a capture and you will get an additional " + getExperienceBoost(4) + "% more experience.<br><br>" +
			"At level 5 you will receive " + getChinsToCapture(5) + " chinchompas a capture and you will get an additional " + getExperienceBoost(5) + "% more experience."
			;
	}
}
