package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TombsSpeedRunner extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Tombs Speed-Runner";
	}

	@Override
	public String getDesc() {
		return "Complete the Tombs of Amascut (normal) within 10 mins at any group size.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
