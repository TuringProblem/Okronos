package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TombLooter extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.tombsOfAmascutKills.getKills() >= 150) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Tomb Looter";
	}

	@Override
	public String getDesc() {
		return "Complete the Tombs of Amascut 150 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
