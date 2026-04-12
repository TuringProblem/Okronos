package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class BobTheBuilder extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public double getExpBoost() {
		return (15 + (7.5 * perkLevel)) / 100;
	}

	public double getMaterialsToSave() {
		return (10 + (7.5 * perkLevel)) / 100;
	}

	public double getMaterialSaveChance() {
		return 15 + (7.5 * perkLevel);
	}

	private double getMaterialSaveChance(int level) {
		return 15 + (7.5 * level);
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Bob The Builder";
	}

	@Override
	public String getPerkDescription() {
		return "Construction boost";
	}

	@Override
	public String getPerkEffect() {
		return "You will receive a " + getExpBoost() * 100 + "% experience boost whilst doing construction.<br>" +
			"You will have a " + getMaterialSaveChance() + "% chance of saving your materials when doing construction.";
	}

	private double materialsToSave(int level) {
		return 10 + (7.5 * level);
	}

	private double experienceBoost(int level) {
		return 15 + (7.5 * level);
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will receive an experience boost in construction and you will" +
			" also save materials when training construction.<br><br>" +
			"At level 1 you will receive a " + experienceBoost(1) + "% experience boost in construction and " +
			"you will have a " + getMaterialSaveChance(1) + "% chance of saving your materials when doing construction.<br><br>" +
			"At level 2 you will receive a " + experienceBoost(2) + "% experience boost in construction and " +
			"you will have a " + getMaterialSaveChance(2) + "% chance of saving your materials when doing construction.<br><br>" +
			"At level 3 you will receive a " + experienceBoost(3) + "% experience boost in construction and " +
			"you will have a " + getMaterialSaveChance(3) + "% chance of saving your materials when doing construction.<br><br>" +
			"At level 4 you will receive a " + experienceBoost(4) + "% experience boost in construction and " +
			"you will have a " + getMaterialSaveChance(4) + "% chance of saving your materials when doing construction.<br><br>" +
			"At level 5 you will receive a " + experienceBoost(5) + "% experience boost in construction and " +
			"you will have a " + getMaterialSaveChance(5) + "% chance of saving your materials when doing construction."
			;
	}
}
