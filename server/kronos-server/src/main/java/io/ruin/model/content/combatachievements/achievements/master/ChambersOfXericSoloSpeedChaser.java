package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ChambersOfXericSoloSpeedChaser extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "CoX (Solo) Speed-Chaser";
	}

	@Override
	public String getDesc() {
		return "Complete a Chambers of Xeric (Solo) in less than 12 minutes.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
