package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class NightmareSoloSpeedChaser extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Nightmare (Solo) Speed-Chaser";
	}

	@Override
	public String getDesc() {
		return "Defeat The Nightmare in solo mode in under 2 minutes.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
