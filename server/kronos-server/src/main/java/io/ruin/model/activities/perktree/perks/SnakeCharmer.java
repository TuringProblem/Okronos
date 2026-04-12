package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;
import io.ruin.model.entity.player.Player;

public class SnakeCharmer extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	public double getDamageMultiplier() {
		return (7.5 * perkLevel) / 100;
	}

	public double getAdditionalDropChance() {
		return 2.5 + (4.5 * perkLevel) / 100;
	}

	@Override
	public String getPerkName() {
		return "Snake Charmer";
	}

	@Override
	public String getPerkDescription() {
		return "Zulrah boost.";
	}

	@Override
	public String getPerkEffect() {
		return "You will receive a " + ((int) (getDamageMultiplier() * 100)) + "% damage boost when fighting zulrah.<br>" +
			"You will also receive double scales and a chance for an additional drop roll.";
	}

	private double getDamageMultiplier(int level) {
		return 7.5 * level;
	}

	private double getAdditionalDropChance(int level) {
		return 2.5 + (4.5 * level);
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will have a damage boost when fighting Zulrah, you will also receive double the zulrah's scales and a chance for an additional drop roll.<br><br>" +
			"At level 1 when fighting Zulrah you will have a " + getDamageMultiplier(1) +
			"% damage boost, you will receive double zulrah's scales dropped" +
			" and you will have a chance of " + getAdditionalDropChance(1) +
			"% chance for an additional drop roll from Zulrah.<br><br>" +
			"At level 2 when fighting Zulrah you will have a " + getDamageMultiplier(2) +
			"% damage boost, you will receive double zulrah's scales dropped" +
			" and you will have a chance of " + getAdditionalDropChance(2) +
			"% chance for an additional drop roll from Zulrah.<br><br>" +
			"At level 3 when fighting Zulrah you will have a " + getDamageMultiplier(3) +
			"% damage boost, you will receive double zulrah's scales dropped" +
			" and you will have a chance of " + getAdditionalDropChance(3) +
			"% chance for an additional drop roll from Zulrah.<br><br>" +
			"At level 4 when fighting Zulrah you will have a " + getDamageMultiplier(4) +
			"% damage boost, you will receive double zulrah's scales dropped" +
			" and you will have a chance of " + getAdditionalDropChance(4) +
			"% chance for an additional drop roll from Zulrah.<br><br>" +
			"At level 5 when fighting Zulrah you will have a " + getDamageMultiplier(5) +
			"% damage boost, you will receive double zulrah's scales dropped" +
			" and you will have a chance of " + getAdditionalDropChance(5) +
			"% chance for an additional drop roll from Zulrah."
			;
	}
}
