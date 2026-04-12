package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ChambersOfXericTrioSpeedRunner extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "CoX (Trio) Speed-Runner";
	}

	@Override
	public String getDesc() {
		return "Complete a Chambers of Xeric (Trio) in less than 8 minutes 30 seconds.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
