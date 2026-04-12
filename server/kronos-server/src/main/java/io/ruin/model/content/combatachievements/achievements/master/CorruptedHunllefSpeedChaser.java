package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class CorruptedHunllefSpeedChaser extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Corrupted Hunllef Speed-Chaser";
	}

	@Override
	public String getDesc() {
		return "Kill the Corrupted Hunllef in under 2:30.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
