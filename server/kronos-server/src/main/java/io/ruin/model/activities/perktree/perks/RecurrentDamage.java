package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class RecurrentDamage extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public int getRecurrentDamageAmount(int damage) {
		double calc = (25 + (this.perkLevel * 2.5)) / 100;
		return (int) (damage * calc);
	}

	public int getRecurrentDamageChance() {
		return (int) (7.5 + (perkLevel * 5));
	}


	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Recurrent Damage";
	}

	@Override
	public String getPerkDescription() {
		return "Extra damage.";
	}

	@Override
	public String getPerkEffect() {
		return "You will receive a " + getRecurrentDamageChance() +
			"% chance to hit an additional amount of damage based on your initial hit and your perk level.";
	}

	private int getRecurrentDamageChance(int level) {
		return (int) (7.5 + (level * 5));
	}

	private double getRecurrentDamage(int level) {
		double calc = (25 + (level * 2.5)) / 100;
		return (1 * calc);
	}


	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will have a chance to send an additional hit and the damage of this hit will be based on your perk level and the initial hit.<br><br>" +
			"At level 1 you will have a chance of " + getRecurrentDamageChance(1) +
			"% to send an additional hit and the damage will be at a ratio of 1:" + getRecurrentDamage(1) + ".<br><br>" +
			"At level 2 you will have a chance of " + getRecurrentDamageChance(2) +
			"% to send an additional hit and the damage will be at a ratio of 1:" + getRecurrentDamage(2) + ".<br><br>" +
			"At level 3 you will have a chance of " + getRecurrentDamageChance(3) +
			"% to send an additional hit and the damage will be at a ratio of 1:" + getRecurrentDamage(3) + ".<br><br>" +
			"At level 4 you will have a chance of " + getRecurrentDamageChance(4) +
			"% to send an additional hit and the damage will be at a ratio of 1:" + getRecurrentDamage(4) + ".<br><br>" +
			"At level 5 you will have a chance of " + getRecurrentDamageChance(5) +
			"% to send an additional hit and the damage will be at a ratio of 1:" + getRecurrentDamage(5) + "."
			;
	}
}
