package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TombsOfAmascutChampion extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.tombsOfAmascutKills.getKills() >= 75) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Tombs of Amascut Champion";
	}

	@Override
	public String getDesc() {
		return "Complete the Tombs of Amascut in Entry mode (or above) 75 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
