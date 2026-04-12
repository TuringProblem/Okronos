package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TombsOfAmascutAdept extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Tombs of Amascut Adept";
	}

	@Override
	public String getDesc() {
		return "Complete the Tombs of Amascut once.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
