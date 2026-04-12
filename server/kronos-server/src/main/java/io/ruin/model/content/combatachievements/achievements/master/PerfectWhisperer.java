package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerfectWhisperer extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perfect Whisperer";
	}

	@Override
	public String getDesc() {
		return "Kill the Whisperer without taking avoidable damage 5 times without leaving.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
