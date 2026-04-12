package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TheFremennikWay extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "The Fremennik Way";
	}

	@Override
	public String getDesc() {
		return "Kill Vorkath with only your fists.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
