package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ChambersOfXericTrioSpeedChaser extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "CoX (Trio) Speed-Chaser";
	}

	@Override
	public String getDesc() {
		return "Complete a Chambers of Xeric (Trio) in less than 10 minutes.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
