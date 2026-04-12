package io.ruin.model.activities.perktree.perks;

import io.ruin.api.utils.NumberUtils;
import io.ruin.model.activities.perktree.PlayerPerk;

public class RunningPays extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public int getReward() {
		return 75000 * perkLevel;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Running Pays";
	}

	@Override
	public String getPerkDescription() {
		return "Paid agility training";
	}

	@Override
	public String getPerkEffect() {
		return "You will receive " + NumberUtils.formatNumber(getReward()) + " coins whilst completing agility laps.";
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will be paid every time you complete an agility lap.<br><br>" +
			"At level 1 you will receive 75,000 coins per agility lap.<br><br>" +
			"At level 2 you will receive 150,000 coins per agility lap.<br><br>" +
			"At level 3 you will receive 225,000 coins per agility lap.<br><br>" +
			"At level 4 you will receive 300,000 coins per agility lap.<br><br>" +
			"At level 5 you will receive 375,000 coins per agility lap."
			;
	}
}
