package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class LeviathanAdept extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Leviathan Adept";
	}

	@Override
	public String getDesc() {
		return "Kill the Leviathan once.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
