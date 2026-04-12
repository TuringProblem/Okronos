package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TheatreFiveScaleSpeedRunner extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Theatre (5-Scale) Speed-Runner";
	}

	@Override
	public String getDesc() {
		return "Complete the Theatre of Blood (5-scale) in less than 7 minutes.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
