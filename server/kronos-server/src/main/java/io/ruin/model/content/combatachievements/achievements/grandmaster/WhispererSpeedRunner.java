package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class WhispererSpeedRunner extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "The Whisperer Speed-Runner";
	}

	@Override
	public String getDesc() {
		return "Kill The Whisperer in under 30 seconds without a slayer task.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
