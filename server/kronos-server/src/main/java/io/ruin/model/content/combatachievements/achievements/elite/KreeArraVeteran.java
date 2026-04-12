package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class KreeArraVeteran extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.kreeArraKills.getKills() >= 150) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Kree'arra Veteran";
	}

	@Override
	public String getDesc() {
		return "Kill Kree'arra 150 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
