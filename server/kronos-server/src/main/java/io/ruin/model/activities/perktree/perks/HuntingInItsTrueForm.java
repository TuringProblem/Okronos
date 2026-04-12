package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class HuntingInItsTrueForm extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	public int getChanceToCollectCapturedCreature() {
		return (int) (7.5 + (7.5 * perkLevel));
	}

	@Override
	public String getPerkName() {
		return "Hunting In Its True Form";
	}

	@Override
	public String getPerkDescription() {
		return "Trap magician";
	}

	@Override
	public String getPerkEffect() {
		return "You have a " + getChanceToCollectCapturedCreature() + "% chance for the trap to be automatically" +
			" looted and reset.";
	}

	private double getChance(int level) {
		return 7.5 + (7.5 * level);
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will have a chance for your trap to automatically loot and reset your hunter trap when you capture a creature.<br><br>" +
			"At level 1 you will have a " + getChance(1) + "% chance for your trap to be looted automatically and for the trap to be reset.<br><br>" +
			"At level 2 you will have a " + getChance(2) + "% chance for your trap to be looted automatically and for the trap to be reset.<br><br>" +
			"At level 3 you will have a " + getChance(3) + "% chance for your trap to be looted automatically and for the trap to be reset.<br><br>" +
			"At level 4 you will have a " + getChance(4) + "% chance for your trap to be looted automatically and for the trap to be reset.<br><br>" +
			"At level 5 you will have a " + getChance(5) + "% chance for your trap to be looted automatically and for the trap to be reset."
			;
	}
}
