package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.player.Player;

public class KalphiteQueenAdept extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Kalphite Queen Adept";
	}

	@Override
	public String getDesc() {
		return "Kill the Kalphite Queen once.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}
}
