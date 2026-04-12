package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class FinestWizardry extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public double getExtraHitChance() {
		return (7.5 + (perkLevel * 10)) / 100;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Finest Wizardry";
	}

	@Override
	public String getPerkDescription() {
		return "Magic double hit";
	}

	@Override
	public String getPerkEffect() {
		return "You have a " + ((int) (getExtraHitChance() * 100)) + "% chance to fire another magic attack if your attack was successful.";
	}

	private double getExtraHitChance(int level) {
		return 7.5 + (level * 10);
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will have a chance to fire another magic attack just after your first one if the hit is successful.<br><br>" +
			"At level 1 you will have a " + getExtraHitChance(1) + "% chance to fire an additional attack.<br><br>" +
			"At level 2 you will have a " + getExtraHitChance(2) + "% chance to fire an additional attack.<br><br>" +
			"At level 3 you will have a " + getExtraHitChance(3) + "% chance to fire an additional attack.<br><br>" +
			"At level 4 you will have a " + getExtraHitChance(4) + "% chance to fire an additional attack.<br><br>" +
			"At level 5 you will have a " + getExtraHitChance(5) + "% chance to fire an additional attack."
			;
	}
}
