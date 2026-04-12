package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class RaidingRestorations extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public double getDamageReduction() {
		return (7.5 + (2.5 * perkLevel)) / 100;
	}

	public double getLootChance() {
		return (2.5 + (3 * perkLevel)) / 100;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Raiding Restorations";
	}

	@Override
	public String getPerkDescription() {
		return "Raiding boost.";
	}

	@Override
	public String getPerkEffect() {
		return "You have " + getLootChance() * 100 + "% better chance at receiving purples.<br> " +
			"You will also take " + getDamageReduction() * 100 + "% less damage.";
	}

	private double getLootChance(int level) {
		return 2.5 + (3 * level);
	}

	private double getDamageReduction(int level) {
		return 7.5 + (2.5 * level);
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will have a better chance at receiving purple when raiding and you will also take less damage when raiding.<br><br>" +
			"At level 1 you will have a " + getLootChance(1) + "% better chance at receiving purples and you will also take " + getDamageReduction(1) + "% less damage within raids.<br><br>" +
			"At level 2 you will have a " + getLootChance(2) + "% better chance at receiving purples and you will also take " + getDamageReduction(2) + "% less damage within raids.<br><br>" +
			"At level 3 you will have a " + getLootChance(3) + "% better chance at receiving purples and you will also take " + getDamageReduction(3) + "% less damage within raids.<br><br>" +
			"At level 4 you will have a " + getLootChance(4) + "% better chance at receiving purples and you will also take " + getDamageReduction(4) + "% less damage within raids.<br><br>" +
			"At level 5 you will have a " + getLootChance(5) + "% better chance at receiving purples and you will also take " + getDamageReduction(5) + "% less damage within raids.<br><br>"
			;
	}
}
