package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class WhoIsTheKingNow extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Who Is the King Now?";
	}

	@Override
	public String getDesc() {
		return "Kill The King Black Dragon 10 times in a private instance without leaving the instance.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}
}
