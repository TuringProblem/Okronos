package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TheatreOfBloodAdept extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Theatre of Blood Adept";
	}

	@Override
	public String getDesc() {
		return "Complete the Theatre of Blood once.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}
}
