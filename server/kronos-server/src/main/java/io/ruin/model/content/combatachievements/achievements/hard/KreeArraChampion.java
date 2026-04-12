package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class KreeArraChampion extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.kreeArraKills.getKills() >= 50) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Kree'arra Champion";
	}

	@Override
	public String getDesc() {
		return "Kill Kree'arra 50 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
