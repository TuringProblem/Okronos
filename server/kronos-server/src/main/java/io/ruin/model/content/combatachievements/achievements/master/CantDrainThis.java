package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.player.Player;

public class CantDrainThis extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Can't Drain This";
	}

	@Override
	public String getDesc() {
		return "Kill The Maiden of Sugadinti without anyone in the team being drained of any prayer points.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
