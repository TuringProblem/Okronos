package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class EgniolDiet extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Egniol Diet";
	}

	@Override
	public String getDesc() {
		return "Kill the Crystalline Hunllef without making an egniol potion within the Gauntlet.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
