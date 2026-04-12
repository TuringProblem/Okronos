package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class LeviathanSpeedTrialist extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Leviathan Speed-Trialist";
	}

	@Override
	public String getDesc() {
		return "Kill the Leviathan in less than 1:00 without a slayer task.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
