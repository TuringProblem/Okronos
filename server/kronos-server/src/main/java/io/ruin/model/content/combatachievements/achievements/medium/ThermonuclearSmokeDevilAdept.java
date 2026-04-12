package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ThermonuclearSmokeDevilAdept extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Thermonuclear Smoke Devil Adept";
	}

	@Override
	public String getDesc() {
		return "Kill a Thermonuclear Smoke Devil.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}
}
