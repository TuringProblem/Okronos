package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class WorkingOvertime extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Working Overtime";
	}

	@Override
	public String getDesc() {
		return "Kill the Alchemical Hydra 30 times without leaving the room.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
