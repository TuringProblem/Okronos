package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ChaosElementalVeteran extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.chaosElementalKills.getKills() >= 150) {
			completed = true;
			complete(player);
		}
	}


	@Override
	public String getName() {
		return "Chaos Elemental Veteran";
	}

	@Override
	public String getDesc() {
		return "Kill the Chaos Elemental 150 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}

}
