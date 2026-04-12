package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class ArcaneEnhancement extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public double getDamageBoost() {
		return (10 + (perkLevel * 3.75)) / 100;
	}


	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Arcane Enhancement";
	}

	@Override
	public String getPerkDescription() {
		return "Attack faster with magic spells";
	}

	@Override
	public String getPerkEffect() {
		return "When using magic your damage will be boosted by " + getDamageBoost() * 100 + " %.";
	}

	private double getDamageBoost(int level) {
		return 10 + (level * 3.75);
	}


	@Override
	public String getRepositoryDescription() {
		return "When using magic with this perk active your max damage will be increased.<br><br>" +
			"At level 1 your max hit will be boosted by " + getDamageBoost(1) + "%.<br><br>" +
			"At level 2 your max hit will be boosted by " + getDamageBoost(2) + "%.<br><br>" +
			"At level 3 your max hit will be boosted by " + getDamageBoost(3) + "%.<br><br>" +
			"At level 4 your max hit will be boosted by " + getDamageBoost(4) + "%.<br><br>" +
			"At level 5 your max hit will be boosted by " + getDamageBoost(5) + "%.";
	}
}
