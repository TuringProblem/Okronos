package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class SolHereditSpeedRunner extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Sol Heredit Speed-Runner";
	}

	@Override
	public String getDesc() {
		return "Kill Sol Heredit in under 35 seconds whilst not on a slayer task.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
