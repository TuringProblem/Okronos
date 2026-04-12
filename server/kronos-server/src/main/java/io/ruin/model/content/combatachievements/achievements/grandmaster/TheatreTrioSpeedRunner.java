package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TheatreTrioSpeedRunner extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Theatre (Trio) Speed-Runner";
	}

	@Override
	public String getDesc() {
		return "Complete the Theatre of Blood (Trio) in less than 8 minutes and 30 seconds.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
