package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class ASlayingExperience extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public float getBoost() {
		return 7.5f * perkLevel / 100;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "A Slaying Experience";
	}

	@Override
	public String getPerkDescription() {
		return "Boost when slaying";
	}

	@Override
	public String getPerkEffect() {
		return "You will receive a " + ((int) (getBoost() * 100)) + "% experience boost in slayer.<br>You will receive a "
			+ ((int) (getBoost() * 100)) + "% attack and damage boost against your task.";
	}

	private double boost(int level) {
		return 7.5 * level;
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will receive an accuracy and damage boost against your slayer task and you will also receive an experience boost in slayer. <br><br>" +
			"At level 1 you will receive an accuracy and damage boost of " + boost(1)
			+ "% and you will also receive a " + boost(1) + "% experience boost in slayer.<br><br>" +
			"At level 2 you will receive an accuracy and damage boost of " + boost(2)
			+ "% and you will also receive a " + boost(2) + "% experience boost in slayer.<br><br>" +
			"At level 3 you will receive an accuracy and damage boost of " + boost(3)
			+ "% and you will also receive a " + boost(3) + "% experience boost in slayer.<br><br>" +
			"At level 4 you will receive an accuracy and damage boost of " + boost(4)
			+ "% and you will also receive a " + boost(4) + "% experience boost in slayer.<br><br>" +
			"At level 5 you will receive an accuracy and damage boost of " + boost(5)
			+ "% and you will also receive a " + boost(5) + "% experience boost in slayer."
			;
	}
}
