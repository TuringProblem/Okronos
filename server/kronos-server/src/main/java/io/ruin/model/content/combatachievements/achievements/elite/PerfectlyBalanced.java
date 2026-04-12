package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerfectlyBalanced extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perfectly Balanced";
	}

	@Override
	public String getDesc() {
		return "Kill the Vanguards without them resetting their health.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
