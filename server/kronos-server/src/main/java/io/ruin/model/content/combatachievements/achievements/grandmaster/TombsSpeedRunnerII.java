package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TombsSpeedRunnerII extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Tombs Speed Runner II";
	}

	@Override
	public String getDesc() {
		return "Complete the Tombs of Amascut (expert) within 10 mins at any group size.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
