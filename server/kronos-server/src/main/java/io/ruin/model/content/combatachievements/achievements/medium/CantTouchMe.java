package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.player.Player;

public class CantTouchMe extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Can't Touch Me";
	}

	@Override
	public String getDesc() {
		return "Kill all six Barrows Brothers without letting any of the melee brothers attack you with melee.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}
}
