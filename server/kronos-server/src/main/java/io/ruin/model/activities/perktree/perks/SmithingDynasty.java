package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class SmithingDynasty extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public int chanceToKeepMaterials() {
		return (int) (7.5 + (perkLevel * 5));
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Smithing Dynasty";
	}

	@Override
	public String getPerkDescription() {
		return "Smithing saver.";
	}

	@Override
	public String getPerkEffect() {
		return "You will receive a " + chanceToKeepMaterials() + "% chance to keep your materials when smithing.";
	}

	private int chanceToKeepMaterials(int level) {
		return (int) (7.5 + (level * 5));
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will have a chance of keeping the materials when smithing items.<br><br>" +
			"At level 1 you will have a chance of " + chanceToKeepMaterials(1) + "% to keep your materials when smithing an item.<br><br>" +
			"At level 2 you will have a chance of " + chanceToKeepMaterials(2) + "% to keep your materials when smithing an item.<br><br>" +
			"At level 3 you will have a chance of " + chanceToKeepMaterials(3) + "% to keep your materials when smithing an item.<br><br>" +
			"At level 4 you will have a chance of " + chanceToKeepMaterials(4) + "% to keep your materials when smithing an item.<br><br>" +
			"At level 5 you will have a chance of " + chanceToKeepMaterials(5) + "% to keep your materials when smithing an item."
			;
	}
}
