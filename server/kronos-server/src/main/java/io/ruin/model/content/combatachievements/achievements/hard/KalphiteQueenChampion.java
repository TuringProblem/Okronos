package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class KalphiteQueenChampion extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.kalphiteQueenKills.getKills() >= 50) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Kalphite Queen Champion";
	}

	@Override
	public String getDesc() {
		return "Kill the Kalphite Queen 50 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
