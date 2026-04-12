package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class AlchemicalMaster extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.alchemicalHydraKills.getKills() >= 300) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Alchemical Master";
	}

	@Override
	public String getDesc() {
		return "Kill the Alchemical Hydra 300 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
