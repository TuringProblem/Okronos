package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class TheDragonSlayer extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	public float getDamageMultiplier() {
		return (5.0f * perkLevel) / 100;
	}

	@Override
	public String getPerkName() {
		return "The Dragon Slayer";
	}

	@Override
	public String getPerkDescription() {
		return "Boost against dragons.";
	}

	@Override
	public String getPerkEffect() {
		return "You will receive a " + getDamageMultiplier() * 100 + "% damage and accuracy boost against dragons.";
	}

	private float getDamageMultiplier(int level) {
		return 5 * level;
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will have a damage boost against dragons.<br><br>" +
			"At level 1 you will have a damage boost of " + getDamageMultiplier(1) + "% when fighting any dragon.<br><br>" +
			"At level 2 you will have a damage boost of " + getDamageMultiplier(2) + "% when fighting any dragon.<br><br>" +
			"At level 3 you will have a damage boost of " + getDamageMultiplier(3) + "% when fighting any dragon.<br><br>" +
			"At level 4 you will have a damage boost of " + getDamageMultiplier(4) + "% when fighting any dragon.<br><br>" +
			"At level 5 you will have a damage boost of " + getDamageMultiplier(5) + "% when fighting any dragon."
			;
	}
}
