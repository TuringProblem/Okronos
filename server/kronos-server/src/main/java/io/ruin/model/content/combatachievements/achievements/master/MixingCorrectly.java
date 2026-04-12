package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class MixingCorrectly extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Mixing Correctly";
	}

	@Override
	public String getDesc() {
		return "Kill the Alchemical Hydra without empowering it.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
