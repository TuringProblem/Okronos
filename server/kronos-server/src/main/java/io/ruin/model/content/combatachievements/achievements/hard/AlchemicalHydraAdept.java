package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class AlchemicalHydraAdept extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Alchemical Hydra Adept";
	}

	@Override
	public String getDesc() {
		return "Kill the Alchemical Hydra once.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
