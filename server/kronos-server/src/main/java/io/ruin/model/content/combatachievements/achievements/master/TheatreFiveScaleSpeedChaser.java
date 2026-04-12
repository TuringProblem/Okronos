package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TheatreFiveScaleSpeedChaser extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Theatre (5-Scale) Speed-Chaser";
	}

	@Override
	public String getDesc() {
		return "Complete the Theatre of Blood (5-scale) in less than 8 minutes.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
