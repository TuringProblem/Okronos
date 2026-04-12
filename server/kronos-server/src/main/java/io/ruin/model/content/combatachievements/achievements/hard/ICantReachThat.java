package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ICantReachThat extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "I Can't Reach That";
	}

	@Override
	public String getDesc() {
		return "Kill Scorpia without taking any damage from her.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
