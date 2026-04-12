package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class KitchenExpert extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public double successFullyCookBoost() {
		return (perkLevel * 17.5) / 100;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Kitchen Expert";
	}

	@Override
	public String getPerkDescription() {
		return "Cooking boost.";
	}

	@Override
	public String getPerkEffect() {
		return "You have a " + ((int) (successFullyCookBoost() * 100)) + "% additional chance to successfully cook your food.";
	}

	private double getChance(int level) {
		return (level * 17.5);
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will have a higher chance to successfully cook the food you are cooking.<br><br>" +
			"At level 1 you will have an additional " + getChance(1) + "% chance to successfully cook the food you are cooking.<br><br>" +
			"At level 2 you will have an additional " + getChance(2) + "% chance to successfully cook the food you are cooking.<br><br>" +
			"At level 3 you will have an additional " + getChance(3) + "% chance to successfully cook the food you are cooking.<br><br>" +
			"At level 4 you will have an additional " + getChance(4) + "% chance to successfully cook the food you are cooking.<br><br>" +
			"At level 5 you will have an additional " + getChance(5) + "% chance to successfully cook the food you are cooking."
			;
	}
}
