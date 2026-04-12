package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TheatreFourScaleSpeedChaser extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Theatre (4-Scale) Speed-Chaser";
	}

	@Override
	public String getDesc() {
		return "Complete the Theatre of Blood (4-scale) in less than 9 minutes.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
