package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class InfernoSpeedRunner extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Inferno Speed-Runner";
	}

	@Override
	public String getDesc() {
		return "Complete the Inferno in less than 10 minutes.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
