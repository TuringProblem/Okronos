package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class GrotesqueGuardiansSpeedChaser extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Grotesque Guardians Speed-Chaser";
	}

	@Override
	public String getDesc() {
		return "Kill the Grotesque Guardians in less than 45 seconds.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
