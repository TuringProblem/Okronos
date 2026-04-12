package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TombsSpeedRunnerIII extends CombatAchievement {

	@Override
	public void check(Player player) {

	}

	@Override
	public String getName() {
		return "Tombs Speed Runner III";
	}

	@Override
	public String getDesc() {
		return "Complete the Tombs of Amascut (expert) within 10 mins with a group size of 5 or more.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
