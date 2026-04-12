package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class ThePetHunter extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public double getPetChanceBoost() {
		double calc = 2.5 + (8.5 * perkLevel);
		calc /= 100;
		return 1 - calc;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "The Pet Hunter";
	}

	@Override
	public String getPerkDescription() {
		return "Pet rates boosted.";
	}

	@Override
	public String getPerkEffect() {
		return "You will receive a " + ((int) (100 - (getPetChanceBoost() * 100))) + "% boost to pet rates.";
	}

	private double getPetChanceBoost(int level) {
		return 2.5 + (8.5 * level);
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will have a boosted chance at receiving pets.<br><br>" +
			"At level 1 you will be " + getPetChanceBoost(1) + "% more likely to receive a pet.<br><br>" +
			"At level 2 you will be " + getPetChanceBoost(2) + "% more likely to receive a pet.<br><br>" +
			"At level 3 you will be " + getPetChanceBoost(3) + "% more likely to receive a pet.<br><br>" +
			"At level 4 you will be " + getPetChanceBoost(4) + "% more likely to receive a pet.<br><br>" +
			"At level 5 you will be " + getPetChanceBoost(5) + "% more likely to receive a pet."
			;
	}
}
