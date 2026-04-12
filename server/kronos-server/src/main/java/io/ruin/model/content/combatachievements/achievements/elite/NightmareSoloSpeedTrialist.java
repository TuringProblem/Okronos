package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class NightmareSoloSpeedTrialist extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Nightmare (Solo) Speed-Trialist";
	}

	@Override
	public String getDesc() {
		return "Defeat the Nightmare (Solo) in less than 3 minutes.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
