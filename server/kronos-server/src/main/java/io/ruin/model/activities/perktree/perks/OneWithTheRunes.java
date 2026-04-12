package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class OneWithTheRunes extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public double getRuneMultiplier() {
		return (25 + (this.perkLevel * 12.5)) / 100;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "One With The Runes";
	}

	@Override
	public String getPerkDescription() {
		return "Runes multiplied runecrafting.";
	}

	@Override
	public String getPerkEffect() {
		return "You will receive an additional " + ((int) (getRuneMultiplier() * 100)) + "% of runes whilst runecrafting.";
	}

	public double getRuneMultiplier(int level) {
		return 25 + (level * 12.5);
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will receive more runes per ess whilst runecrafting.<br><br>" +
			"At level 1 you will receive " + getRuneMultiplier(1) + "% more runes when runecrafting.<br><br>" +
			"At level 2 you will receive " + getRuneMultiplier(2) + "% more runes when runecrafting.<br><br>" +
			"At level 3 you will receive " + getRuneMultiplier(3) + "% more runes when runecrafting.<br><br>" +
			"At level 4 you will receive " + getRuneMultiplier(4) + "% more runes when runecrafting.<br><br>" +
			"At level 5 you will receive " + getRuneMultiplier(5) + "% more runes when runecrafting."
			;
	}
}
