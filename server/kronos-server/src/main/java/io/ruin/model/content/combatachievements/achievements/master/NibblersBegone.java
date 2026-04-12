package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class NibblersBegone extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Nibblers, Begone!";
	}

	@Override
	public String getDesc() {
		return "Kill Tzkal-Zuk without letting a pillar fall before wave 67.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
