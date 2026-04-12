package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TombRaider extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.tombsOfAmascutKills.getKills() >= 300) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Tomb Raider";
	}

	@Override
	public String getDesc() {
		return "Complete the Tombs of Amascut 300 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
