package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class HardcoreRaiders extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Hardcore Raiders";
	}

	@Override
	public String getDesc() {
		return "Complete the Tombs of Amascut in a group of two or more without anyone dying.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
