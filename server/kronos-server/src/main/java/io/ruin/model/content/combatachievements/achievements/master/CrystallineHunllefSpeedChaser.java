package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class CrystallineHunllefSpeedChaser extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Crystalline Hunllef Speed-Chaser";
	}

	@Override
	public String getDesc() {
		return "Kill the Crystalline Hunllef in under 1:30.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
