package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class HalfwayThere extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Half-Way There";
	}

	@Override
	public String getDesc() {
		return "Kill a Jal-Zek within the Inferno.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
