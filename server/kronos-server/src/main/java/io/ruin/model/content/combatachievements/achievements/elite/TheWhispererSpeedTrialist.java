package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TheWhispererSpeedTrialist extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "The Whisperer Speed-Trialist";
	}

	@Override
	public String getDesc() {
		return "Kill the Whisperer in less than 0:50 without a slayer task.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
