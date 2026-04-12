package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class Sportsmanship extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Sportsmanship";
	}

	@Override
	public String getDesc() {
		return "Defeat Sol Heredit once.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
