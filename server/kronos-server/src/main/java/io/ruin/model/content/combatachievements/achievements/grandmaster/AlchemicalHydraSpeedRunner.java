package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class AlchemicalHydraSpeedRunner extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Alchemical Hydra Speed-Runner";
	}

	@Override
	public String getDesc() {
		return "Kill the Alchemical Hydra in less than 30 seconds.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
