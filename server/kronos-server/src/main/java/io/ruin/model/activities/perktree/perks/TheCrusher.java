package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;
import io.ruin.model.entity.player.Player;

public class TheCrusher extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public double getAccuracyBoost() {
		return (perkLevel * 12.5) / 100;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "The Crusher";
	}

	@Override
	public String getPerkDescription() {
		return "Crush weapons boost.";
	}

	@Override
	public String getPerkEffect() {
		return "You will receive a " + getAccuracyBoost() * 100 + "% accuracy boost using crush weapons.";
	}

	private double getAccuracyBoost(int level) {
		return (level * 12.5);
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will have boosted accuracy when using crush weapons.<br><br>" +
			"At level 1 you will have an additional accuracy of " + getAccuracyBoost(1) + "% when using crush weapons.<br><br>" +
			"At level 2 you will have an additional accuracy of " + getAccuracyBoost(2) + "% when using crush weapons.<br><br>" +
			"At level 3 you will have an additional accuracy of " + getAccuracyBoost(3) + "% when using crush weapons.<br><br>" +
			"At level 4 you will have an additional accuracy of " + getAccuracyBoost(4) + "% when using crush weapons.<br><br>" +
			"At level 5 you will have an additional accuracy of " + getAccuracyBoost(5) + "% when using crush weapons."
			;
	}
}
