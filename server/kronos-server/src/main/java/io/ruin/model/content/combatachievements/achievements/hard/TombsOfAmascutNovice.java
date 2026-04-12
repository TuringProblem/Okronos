package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TombsOfAmascutNovice extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.tombsOfAmascutKills.getKills() >= 25) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Tombs of Amascut Novice";
	}

	@Override
	public String getDesc() {
		return "Complete the Tombs of Amascut 25 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}
}
