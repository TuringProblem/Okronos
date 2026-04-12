package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class GalvekSpeedTrialist extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Galvek Speed-Trialist";
	}

	@Override
	public String getDesc() {
		return "Kill Galvek in less than 2 minutes without a slayer task.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
