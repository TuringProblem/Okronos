package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class TheGoldenGauntlet extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public double getDamageBoost() {
		return (5.0 * perkLevel) / 100;
	}

	public double getDamageReduction() {
		return (7.5 + (7.5 * perkLevel)) / 100;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "The Golden Gauntlet";
	}

	@Override
	public String getPerkDescription() {
		return "Boost within The Gauntlet.";
	}

	@Override
	public String getPerkEffect() {
		return "You will take " + getDamageReduction() * 100 + "% less damage within the gauntlet and you" +
			" will deal " + getDamageBoost() * 100 + "% more damage within The Gauntlet.<br><br>" +
			"At level 5 you will be fully equipped upon entering the gauntlet with perfected gear and supplies.";
	}

	private double getDamageBoost(int level) {
		return (5.0 * level);
	}

	private double getDamageReduction(int level) {
		return 7.5 + (7.5 * level);
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will deal more damage and take less damage against creatures within The Gauntlet.<br><br>" +
			"When this perk is level 5 you will be fully equipped upon entering the gauntlet with perfected gear and supplies.<br><br>" +
			"At level 1 you will take " + getDamageReduction(1) + "% less damage and deal " + getDamageBoost(1) + "% more damage within The Gauntlet.<br><br>" +
			"At level 2 you will take " + getDamageReduction(2) + "% less damage and deal " + getDamageBoost(2) + "% more damage within The Gauntlet.<br><br>" +
			"At level 3 you will take " + getDamageReduction(3) + "% less damage and deal " + getDamageBoost(3) + "% more damage within The Gauntlet.<br><br>" +
			"At level 4 you will take " + getDamageReduction(4) + "% less damage and deal " + getDamageBoost(4) + "% more damage within The Gauntlet.<br><br>" +
			"At level 5 you will take " + getDamageReduction(5) + "% less damage and deal " + getDamageBoost(5) + "% more damage within The Gauntlet."
			;
	}
}
