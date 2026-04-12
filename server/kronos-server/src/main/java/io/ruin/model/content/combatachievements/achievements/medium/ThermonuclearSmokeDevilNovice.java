package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ThermonuclearSmokeDevilNovice extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.thermonuclearSmokeDevilKills.getKills() >= 10) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Thermonuclear Smoke Devil Novice";
	}

	@Override
	public String getDesc() {
		return "Kill a Thermonuclear Smoke Devil 10 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}

}
