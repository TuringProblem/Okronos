package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerfectSotetseg extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perfect Sotetseg";
	}

	@Override
	public String getDesc() {
		return "Kill Sotetseg without anyone in the team stepping on the wrong tile in the maze, without getting hit by the tornado and without taking any damage from Sotetseg's attacks whilst off prayer.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
