package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TheatreTrioSpeedChaser extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Theatre (Trio) Speed-Chaser";
	}

	@Override
	public String getDesc() {
		return "Complete the Theatre of Blood (Trio) in less than 10 minutes.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
