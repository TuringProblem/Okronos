package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class CrimsonChin extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public double getChinDamageBoost() {
		double calc = (5.5 * perkLevel) / 100;
		return 1 + calc;
	}

	public double getExtraTargetAmount() {
		double calc = (perkLevel * 15) / 100;
		return 1 + calc;
	}

	public double getChinAccuracyBoost() {
		return (12.5 * perkLevel) / 100;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Crimson Chin";
	}

	@Override
	public String getPerkDescription() {
		return "Enhanced chinchompas";
	}

	@Override
	public String getPerkEffect() {
		return "You will hit an additional " + getExtraTargetAmount() * 100 + "% targets.<br>" +
			"You will receive a " + (int) (((getChinDamageBoost() * 100) - 100)) + "% damage boost with chinchompas.<br>" +
			"You will receive a " + getChinAccuracyBoost() * 100 + "% accurcy boost with chinchompas.";
	}

	//method for getting accuracy boost with level in the params
	private double getChinAccuracy(int level) {
		return (12.5 * level);
	}

	private double getExtraTargetAmount(int level) {
		return (level * 15);
	}

	private double damageBoost(int level) {
		return 2.5 * level;
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will hit more targets with your chinchompas" +
			" than usual, you will also have a higher max hit and have increased accuracy with chinchompas.<br><br>" +
			"At level 1 your chinchompas will have a " + getChinAccuracy(1) +
			"% accuracy boost, you will also receive a " + damageBoost(1) + "% damage boost and hit " +
			getExtraTargetAmount(1) + "% more targets.<br><br>" +
			"At level 2 your chinchompas will have a " + getChinAccuracy(2) +
			"% accuracy boost, you will also receive a " + damageBoost(2) + "% damage boost and hit " +
			getExtraTargetAmount(2) + "% more targets.<br><br>" +
			"At level 3 your chinchompas will have a " + getChinAccuracy(3) +
			"% accuracy boost, you will also receive a " + damageBoost(3) + "% damage boost and hit " +
			getExtraTargetAmount(3) + "% more targets.<br><br>" +
			"At level 4 your chinchompas will have a " + getChinAccuracy(4) +
			"% accuracy boost, you will also receive a " + damageBoost(4) + "% damage boost and hit " +
			getExtraTargetAmount(4) + "% more targets.<br><br>" +
			"At level 5 your chinchompas will have a " + getChinAccuracy(5) +
			"% accuracy boost, you will also receive a " + damageBoost(5) + "% damage boost and hit " +
			getExtraTargetAmount(5) + "% more targets."
			;
	}
}
