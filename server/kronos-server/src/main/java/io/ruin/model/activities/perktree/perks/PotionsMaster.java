package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

import java.text.DecimalFormat;

public class PotionsMaster extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public double getSavePotionDoseChance() {
		return (5.0 + (7.5 * perkLevel)) / 100;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Potions Master";
	}

	@Override
	public String getPerkDescription() {
		return "Dose saver.";
	}

	@Override
	public String getPerkEffect() {
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		return "You have a " + Double.parseDouble(decimalFormat.format(getSavePotionDoseChance() * 100)) + "% chance to save a dose when drinking a potion.";
	}

	public double getSavePotionDoseChance(int level) {
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		return Double.parseDouble(decimalFormat.format(5.0 + (7.5 * level)));
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will have a chance to keep the dose you drank when drinking a potion.<br><br>" +
			"At level 1 you will have a chance of " + getSavePotionDoseChance(1) + "% to save the dose.<br><br>" +
			"At level 2 you will have a chance of " + getSavePotionDoseChance(2) + "% to save the dose.<br><br>" +
			"At level 3 you will have a chance of " + getSavePotionDoseChance(3) + "% to save the dose.<br><br>" +
			"At level 4 you will have a chance of " + getSavePotionDoseChance(4) + "% to save the dose.<br><br>" +
			"At level 5 you will have a chance of " + getSavePotionDoseChance(5) + "% to save the dose."
			;
	}
}
