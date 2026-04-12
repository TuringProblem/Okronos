package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ThreeTwoOneRange extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "3, 2, 1 - Range";
	}

	@Override
	public String getDesc() {
		return "Kill the Crystalline Hunllef without taking damage off prayer.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
