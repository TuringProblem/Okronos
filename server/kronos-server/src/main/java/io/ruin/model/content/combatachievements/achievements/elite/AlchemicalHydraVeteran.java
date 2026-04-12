package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class AlchemicalHydraVeteran extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.alchemicalHydraKills.getKills() >= 150) {
			completed = true;
			complete(player);
		}
	}


	@Override
	public String getName() {
		return "Alchemical Hydra Veteran";
	}

	@Override
	public String getDesc() {
		return "Kill the Alchemical Hydra 150 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}


}
