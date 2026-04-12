package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class WildernessHunter extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public double getDamageReduction() {
		return (7.5 + (3.5 * (1.5 * perkLevel))) / 100;
	}

	public double getDamageBoost() {
		return (3.5 + (4.5 * (2.5 * perkLevel))) / 100;
	}

	public double getAccuracyBoost() {
		return (5 + (3.5 * (1.75 * perkLevel))) / 100;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Wilderness Hunter";
	}

	@Override
	public String getPerkDescription() {
		return "Enhanced fighting NPCs.";
	}

	@Override
	public String getPerkEffect() {
		return "You will receive a " + getAccuracyBoost() * 100 + "% accuracy boost and a " + getDamageBoost() * 100 + "% damage boost to NPCs within the wilderness.<br>" +
			"You will also take " + getDamageReduction() * 100 + "% less damage against NPCs within the wilderness.";
	}

	private double getDamageReduction(int level) {
		return 7.5 + (3.5 * (1.5 * level));
	}

	private double getDamageBoost(int level) {
		return 3.5 + (4.5 * (2.5 * perkLevel));
	}

	private double getAccuracyBoost(int level) {
		return 5 + (3.5 * (1.75 * perkLevel));
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will have boosted accuracy and damage against NPCs within the wilderness and you will also take less damage from said NPCs.<br><br>" +
			"At level 1 you will take " + getDamageReduction(1) + "% less damage from NPCs within the wilderness, you will also deal " + getDamageBoost(1) + "% more damage and you will be " + getAccuracyBoost(1) + "% more accurate against NPCs within the wilderness.<br><br>" +
			"At level 2 you will take " + getDamageReduction(2) + "% less damage from NPCs within the wilderness, you will also deal " + getDamageBoost(2) + "% more damage and you will be " + getAccuracyBoost(2) + "% more accurate against NPCs within the wilderness.<br><br>" +
			"At level 3 you will take " + getDamageReduction(3) + "% less damage from NPCs within the wilderness, you will also deal " + getDamageBoost(3) + "% more damage and you will be " + getAccuracyBoost(3) + "% more accurate against NPCs within the wilderness.<br><br>" +
			"At level 4 you will take " + getDamageReduction(4) + "% less damage from NPCs within the wilderness, you will also deal " + getDamageBoost(4) + "% more damage and you will be " + getAccuracyBoost(4) + "% more accurate against NPCs within the wilderness.<br><br>" +
			"At level 5 you will take " + getDamageReduction(5) + "% less damage from NPCs within the wilderness, you will also deal " + getDamageBoost(5) + "% more damage and you will be " + getAccuracyBoost(5) + "% more accurate against NPCs within the wilderness."
			;
	}
}
