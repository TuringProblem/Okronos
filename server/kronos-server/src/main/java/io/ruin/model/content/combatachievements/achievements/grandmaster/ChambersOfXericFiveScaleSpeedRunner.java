package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ChambersOfXericFiveScaleSpeedRunner extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "CoX (5 Scale) Speed-Runner";
	}

	@Override
	public String getDesc() {
		return "Complete a Chambers of Xeric (5 Scale) in less than 7 minutes.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
