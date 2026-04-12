package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class ArtsnCrafts extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	public int getReductionAmount() {
		return (int) Math.round(0.5 * (perkLevel * 0.75));
	}

	public double getExperienceBoost() {
		return (perkLevel * 4.5) / 100;
	}


	@Override
	public String getPerkName() {
		return "Arts 'n' Crafts";
	}

	@Override
	public String getPerkDescription() {
		return "Crafting Connoisseur";
	}

	@Override
	public String getPerkEffect() {
		return "When crafting you will use up to " + getReductionAmount() + " less required materials when crafting certain items.<br>" +
			"You will also receive a " + getExperienceBoost() * 100 + "% boost in experience when crafting.";
	}

	private double getExperienceBoost(int level) {
		return (level * 4.5);
	}

	private int getMaterialsSaved(int level) {
		return (int) Math.round(0.5 * (level * 0.75));
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will use less materials when crafting and, " +
			"you will also receive an experience boost in crafting.<br><br>" +
			"At level 1 you will save " + getMaterialsSaved(1) + " of your materials on certain items and you will receive a "
			+ getExperienceBoost(1) + "% experience boost.<br><br>" +
			"At level 2 you will save " + getMaterialsSaved(2) + " of your materials on certain items and you will receive a "
			+ getExperienceBoost(2) + "% experience boost.<br><br>" +
			"At level 3 you will save " + getMaterialsSaved(3) + " of your materials on certain items and you will receive a "
			+ getExperienceBoost(3) + "% experience boost.<br><br>" +
			"At level 4 you will save " + getMaterialsSaved(4) + " of your materials on certain items and you will receive a "
			+ getExperienceBoost(4) + "% experience boost.<br><br>" +
			"At level 5 you will save " + getMaterialsSaved(5) + " of your materials on certain items and you will receive a "
			+ getExperienceBoost(5) + "% experience boost."
			;
	}
}
