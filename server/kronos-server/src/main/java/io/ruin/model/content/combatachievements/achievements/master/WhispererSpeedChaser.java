package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class WhispererSpeedChaser extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "The Whisperer Speed-Chaser";
	}

	@Override
	public String getDesc() {
		return "Kill The Whisperer in under 40 seconds without a slayer task.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
