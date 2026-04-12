package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class SnakeSnakeSnake extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Snake. Snake!? Snaaaaaake!";
	}

	@Override
	public String getDesc() {
		return "Kill 3 Snakelings simultaneously.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
