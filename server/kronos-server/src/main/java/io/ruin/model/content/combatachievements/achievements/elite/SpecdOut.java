package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class SpecdOut extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Spec'd Out";
	}

	@Override
	public String getDesc() {
		return "Kill the Thermonuclear Smoke Devil using only special attacks.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
