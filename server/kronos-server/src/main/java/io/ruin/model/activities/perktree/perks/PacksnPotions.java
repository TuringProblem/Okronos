package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class PacksnPotions extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public int getChanceToKeepMaterials() {
		return 5 + (perkLevel * 5);
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Packs 'n' Potions";
	}

	@Override
	public String getPerkDescription() {
		return "Herbology Hero.";
	}

	@Override
	public String getPerkEffect() {
		return "You will have a " + getChanceToKeepMaterials() + "% chance to keep your ingredients when mixing potions.";
	}

	public int getChanceToKeepMaterials(int level) {
		return 5 + (level * 5);
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will have a chance to keep the ingredients when mixing potions.<br><br>" +
			"At level 1 you will have a chance of " + getChanceToKeepMaterials(1) + "% chance to keep the ingredients when mixing potions.<br><br>" +
			"At level 2 you will have a chance of " + getChanceToKeepMaterials(2) + "% chance to keep the ingredients when mixing potions.<br><br>" +
			"At level 3 you will have a chance of " + getChanceToKeepMaterials(3) + "% chance to keep the ingredients when mixing potions.<br><br>" +
			"At level 4 you will have a chance of " + getChanceToKeepMaterials(4) + "% chance to keep the ingredients when mixing potions.<br><br>" +
			"At level 5 you will have a chance of " + getChanceToKeepMaterials(5) + "% chance to keep the ingredients when mixing potions."
			;
	}
}
