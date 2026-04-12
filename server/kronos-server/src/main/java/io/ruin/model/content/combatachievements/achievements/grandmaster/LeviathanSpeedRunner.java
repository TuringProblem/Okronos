package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class LeviathanSpeedRunner extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Leviathan Speed-Runner";
	}

	@Override
	public String getDesc() {
		return "Kill the Leviathan in less than 35 seconds without a slayer task.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
