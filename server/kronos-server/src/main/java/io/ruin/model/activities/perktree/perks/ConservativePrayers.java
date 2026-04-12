package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class ConservativePrayers extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public double getDrainAmount() {
		return (perkLevel * 12.5) / 100;
	}


	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Conservative Prayers";
	}

	@Override
	public String getPerkDescription() {
		return "Prayer boost";
	}

	@Override
	public String getPerkEffect() {
		return "Prayer will drain " + getDrainAmount() * 100 + "% slower.";
	}

	private double drainAmount(int level) {
		return level * 7.5;
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active prayer will drain slower.<br><br>" +
			"At level 1 your prayer will drain " + drainAmount(1) + "% slower.<br><br>" +
			"At level 2 your prayer will drain " + drainAmount(2) + "% slower.<br><br>" +
			"At level 3 your prayer will drain " + drainAmount(3) + "% slower.<br><br>" +
			"At level 4 your prayer will drain " + drainAmount(4) + "% slower.<br><br>" +
			"At level 5 your prayer will drain " + drainAmount(5) + "% slower."
			;
	}
}
