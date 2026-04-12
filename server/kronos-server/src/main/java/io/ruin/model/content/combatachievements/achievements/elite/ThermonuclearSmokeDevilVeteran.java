package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ThermonuclearSmokeDevilVeteran extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.thermonuclearSmokeDevilKills.getKills() >= 150) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Thermonuclear Smoke Devil Veteran";
	}

	@Override
	public String getDesc() {
		return "Kill the Thermonuclear Smoke Devil 150 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
