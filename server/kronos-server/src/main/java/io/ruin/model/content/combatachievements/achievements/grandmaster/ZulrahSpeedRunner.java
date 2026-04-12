package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ZulrahSpeedRunner extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Zulrah Speed-Runner";
	}

	@Override
	public String getDesc() {
		return "Kill Zulrah in less than 8 seconds, without a slayer task.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
