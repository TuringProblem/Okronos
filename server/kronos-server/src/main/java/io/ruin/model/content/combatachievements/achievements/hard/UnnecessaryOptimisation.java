package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class UnnecessaryOptimisation extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Unnecessary Optimisation";
	}

	@Override
	public String getDesc() {
		return "Kill the Kraken after killing all four tentacles.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}
}
