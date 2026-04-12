package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class AlcleanicalHydra extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Alcleanical Hydra";
	}

	@Override
	public String getDesc() {
		return "Kill the Alchemical Hydra without taking any damage.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
