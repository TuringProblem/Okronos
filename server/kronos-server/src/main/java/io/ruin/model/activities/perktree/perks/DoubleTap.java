package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class DoubleTap extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public int getDoubleTapChance() {
		return (int) (7.5 + (5 * perkLevel));
	}

	@Override
	public void activatePerk() {
		isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Double Tap";
	}

	@Override
	public String getPerkDescription() {
		return "The Double Shot";
	}

	@Override
	public String getPerkEffect() {
		return "You have a " + getDoubleTapChance() + "% chance to fire 2 arrows/bolts.";
	}

	private double getChance(int level) {
		return 7.5 + (5 * level);
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will have a chance to fire two bolts/arrows at once.<br><br>" +
			"At level 1 you will have a " + getChance(1) + "% chance to fire an additional arrow or bolt.<br><br>" +
			"At level 2 you will have a " + getChance(2) + "% chance to fire an additional arrow or bolt.<br><br>" +
			"At level 3 you will have a " + getChance(3) + "% chance to fire an additional arrow or bolt.<br><br>" +
			"At level 4 you will have a " + getChance(4) + "% chance to fire an additional arrow or bolt.<br><br>" +
			"At level 5 you will have a " + getChance(5) + "% chance to fire an additional arrow or bolt."
			;
	}
}
