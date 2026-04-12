package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class GhostBuster extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Ghost Buster";
	}

	@Override
	public String getDesc() {
		return "Kill cerberus without taking any damage from summoned souls.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
