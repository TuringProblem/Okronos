package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PrayerSmasher extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Prayer Smasher";
	}

	@Override
	public String getDesc() {
		return "Kill the Kalphite Queen using only the Verac's Flail as a weapon.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
