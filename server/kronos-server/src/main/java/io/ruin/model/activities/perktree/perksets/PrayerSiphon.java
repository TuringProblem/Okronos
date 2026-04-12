package io.ruin.model.activities.perktree.perksets;

import io.ruin.model.activities.perktree.PlayerPerkSet;

public class PrayerSiphon extends PlayerPerkSet {

	public double getSiphonMultiplier() {
		return (2.5 + (18.5 * getLevel())) / 100;
	}

	public int getPrayerDrainChance() {
		return 5 + (getLevel() * 5);
	}

	@Override
	public String getPerkSetName() {
		return "Prayer Siphon";
	}

	@Override
	public String getPerkSetDescription() {
		return "Prayer Points Siphon";
	}

	private double getSiphonMultiplier(int level) {
		return (2.5 + (18.5 * level)) / 100;
	}

	private int getPrayerDrainChance(int level) {
		return 5 + (level * 5);
	}

	@Override
	public String getPerkSetEffect() {
		return "When this perk set is active you will have a chance to siphon prayer points based on your recent hit.<br><br>" +
			"At level 1 you will have a " + getPrayerDrainChance(1) * 100 + "% chance to siphon " + getSiphonMultiplier(1) + "% of your recent hit into prayer points.<br><br>" +
			"At level 2 you will have a " + getPrayerDrainChance(2) * 100 + "% chance to siphon " + getSiphonMultiplier(2) + "% of your recent hit into prayer points.<br><br>" +
			"At level 3 you will have a " + getPrayerDrainChance(3) * 100 + "% chance to siphon " + getSiphonMultiplier(3) + "% of your recent hit into prayer points.<br><br>" +
			"At level 4 you will have a " + getPrayerDrainChance(4) * 100 + "% chance to siphon " + getSiphonMultiplier(4) + "% of your recent hit into prayer points.<br><br>" +
			"At level 5 you will have a " + getPrayerDrainChance(5) * 100 + "% chance to siphon " + getSiphonMultiplier(5) + "% of your recent hit into prayer points.<br><br>"
			;
	}
}
