package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class KeepAway extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Keep Away";
	}

	@Override
	public String getDesc() {
		return "Kill General Graardor in a private instance without taking any damage from the boss or bodyguards.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
