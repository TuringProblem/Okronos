package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

import java.text.DecimalFormat;

public class EnhancedMagicalWeapons extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public double getAccuracyBoost() {
		return (15.0 + (perkLevel * 4.5)) / 100;
	}

	public double getDamageBoost() {
		return (10 + (perkLevel * 2.5)) / 100;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Enhanced Magical Weapons";
	}

	@Override
	public String getPerkDescription() {
		return "Attack faster with autocast magic weapons.";
	}

	@Override
	public String getPerkEffect() {
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		return "Autocast weapons such as trident will receive a " + Double.parseDouble(decimalFormat.format(getDamageBoost() * 100)) + "% damage boost and<br>" +
			"a " + getAccuracyBoost() * 100 + "% accuracy boost.";
	}

	private double getAccuracyBoost(int level) {
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		return Double.parseDouble(decimalFormat.format(15 + (level * 4.5)));
	}

	private double getDamageBoost(int level) {
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		return Double.parseDouble(decimalFormat.format(10 + (level * 2.5)));
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active autocast magic weapons such as trident will have an accuracy and damage boost.<br><br>" +
			"At level 1 you will receive a " + getDamageBoost(1) + "% damage boost and a " + getAccuracyBoost(1) +
			"% accuracy boost using autocast magic weapons.<br><br>" +
			"At level 2 you will receive a " + getDamageBoost(2) + "% damage boost and a " + getAccuracyBoost(2) +
			"% accuracy boost using autocast magic weapons.<br><br>" +
			"At level 3 you will receive a " + getDamageBoost(3) + "% damage boost and a " + getAccuracyBoost(3) +
			"% accuracy boost using autocast magic weapons.<br><br>" +
			"At level 4 you will receive a " + getDamageBoost(4) + "% damage boost and a " + getAccuracyBoost(4) +
			"% accuracy boost using autocast magic weapons.<br><br>" +
			"At level 5 you will receive a " + getDamageBoost(5) + "% damage boost and a " + getAccuracyBoost(5) +
			"% accuracy boost using autocast magic weapons."
			;
	}
}
