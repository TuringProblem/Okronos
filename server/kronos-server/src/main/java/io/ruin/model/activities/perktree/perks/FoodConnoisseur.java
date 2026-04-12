package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

import java.text.DecimalFormat;

public class FoodConnoisseur extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	public double getHealMultiplier() {
		return (7.5 + (perkLevel * 5)) / 100;
	}

	public int getSaveFoodChance() {
		return (int) (5 + (perkLevel * 2.5));
	}

	@Override
	public String getPerkName() {
		return "Food Connoisseur";
	}

	@Override
	public String getPerkDescription() {
		return "Food enhancer.";
	}

	@Override
	public String getPerkEffect() {
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		return "You will heal " + Double.parseDouble(decimalFormat.format(getHealMultiplier() * 100)) + "% more than usual.<br>" +
			"You will also have a " + getSaveFoodChance() + "% chance to keep the food you eat.";
	}

	private double getHealMultiplier(int level) {
		return 7.5 + (level * 5);
	}

	private double getChanceToSave(int level) {
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		return Double.parseDouble(decimalFormat.format(5 + (level * 2.5)));
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active your food will heal more than usual and you will have a chance to keep the food you eat.<br><br>" +
			"At level 1 your food will heal " + getHealMultiplier(1) + "% more than usual and you will have a "
			+ getChanceToSave(1) + "% chance to keep your food.<br><br>" +
			"At level 2 your food will heal " + getHealMultiplier(2) + "% more than usual and you will have a "
			+ getChanceToSave(2) + "% chance to keep your food.<br><br>" +
			"At level 3 your food will heal " + getHealMultiplier(3) + "% more than usual and you will have a "
			+ getChanceToSave(3) + "% chance to keep your food.<br><br>" +
			"At level 4 your food will heal " + getHealMultiplier(4) + "% more than usual and you will have a "
			+ getChanceToSave(4) + "% chance to keep your food.<br><br>" +
			"At level 5 your food will heal " + getHealMultiplier(5) + "% more than usual and you will have a "
			+ getChanceToSave(5) + "% chance to keep your food."
			;
	}
}
