package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class SnakeRebound extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Snake Rebound";
	}

	@Override
	public String getDesc() {
		return "Kill Zulrah by using the Vengeance spell as the finishing blow.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
