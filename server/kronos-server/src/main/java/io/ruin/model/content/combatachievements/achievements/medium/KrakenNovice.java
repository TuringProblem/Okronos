package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.player.Player;

public class KrakenNovice extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.krakenKills.getKills() >= 10) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Kraken Novice";
	}

	@Override
	public String getDesc() {
		return "Kill the Kraken 10 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}
}
