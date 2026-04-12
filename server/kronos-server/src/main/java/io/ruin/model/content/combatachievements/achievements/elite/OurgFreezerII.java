package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class OurgFreezerII extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Ourg Freezer II";
	}

	@Override
	public String getDesc() {
		return "Kill General Graardor without him attacking any players.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
