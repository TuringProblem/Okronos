package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class BlessedBolts extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	public double getBoltSpecialChance() {
		return (5 + (perkLevel * 2.5)) / 100;
	}

	@Override
	public String getPerkName() {
		return "Blessed Bolts";
	}

	@Override
	public String getPerkDescription() {
		return "Lucky bolts";
	}

	@Override
	public String getPerkEffect() {
		return "Bolts have a " + ((int) (getBoltSpecialChance() * 100)) + "% better chance to activate the special.";
	}

	private double getChance(int level) {
		return 5 + (level * 2.5);
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will have a higher chance for bolt specials to activate.<br><br>" +
			"At level 1 bolt specials will have a " + getChance(1) + "% better chance to activate.<br><br>" +
			"At level 2 bolt specials will have a " + getChance(2) + "% better chance to activate.<br><br>" +
			"At level 3 bolt specials will have a " + getChance(3) + "% better chance to activate.<br><br>" +
			"At level 4 bolt specials will have a " + getChance(4) + "% better chance to activate.<br><br>" +
			"At level 5 bolt specials will have a " + getChance(5) + "% better chance to activate."
			;
	}
}
