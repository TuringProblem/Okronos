package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class DefenceMatters extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Defence Matters";
	}

	@Override
	public String getDesc() {
		return "Kill General Graardor 2 times consecutively in a private instance without taking any damage from his bodyguards.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
