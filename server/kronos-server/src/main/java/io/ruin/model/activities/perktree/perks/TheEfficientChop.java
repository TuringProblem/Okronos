package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class TheEfficientChop extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public int getDoubleLogsChance() {
		return 15 + (5 * perkLevel);
	}

	@Override
	public void activatePerk() {
		isActive = true;
	}

	@Override
	public String getPerkName() {
		return "The Efficient Chop";
	}

	@Override
	public String getPerkDescription() {
		return "Get more logs woodcutting.";
	}

	@Override
	public String getPerkEffect() {
		return "You will have a " + getDoubleLogsChance() + "% chance to receive double logs when woodcutting.";
	}

	private int getDoubleLogsChance(int level) {
		return 15 + (5 * level);
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will have a chance to get additional logs when woodcutting.<br><br>" +
			"At level 1 you will have a chance of " + getDoubleLogsChance(1) + "% to get additional logs when woodcutting.<br><br>" +
			"At level 2 you will have a chance of " + getDoubleLogsChance(2) + "% to get additional logs when woodcutting.<br><br>" +
			"At level 3 you will have a chance of " + getDoubleLogsChance(3) + "% to get additional logs when woodcutting.<br><br>" +
			"At level 4 you will have a chance of " + getDoubleLogsChance(4) + "% to get additional logs when woodcutting.<br><br>" +
			"At level 5 you will have a chance of " + getDoubleLogsChance(5) + "% to get additional logs when woodcutting."
			;
	}
}
