package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ExpertTombExplorer extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Expert Tomb Explorer";
	}

	@Override
	public String getDesc() {
		return "Complete the Tombs of Amascut (Expert mode) once.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
