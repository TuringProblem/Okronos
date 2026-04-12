package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class LightningLure extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Lightning Lure";
	}

	@Override
	public String getDesc() {
		return "Kill the Alchemical Hydra without being hit by the lightning attack.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
