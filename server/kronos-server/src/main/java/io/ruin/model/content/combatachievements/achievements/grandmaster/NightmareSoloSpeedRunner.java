package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class NightmareSoloSpeedRunner extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Nightmare (Solo) Speed-Runner";
	}

	@Override
	public String getDesc() {
		return "Defeat The Nightmare in solo mode in under 1:30.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
