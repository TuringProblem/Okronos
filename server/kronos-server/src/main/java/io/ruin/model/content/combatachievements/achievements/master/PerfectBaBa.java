package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerfectBaBa extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perfect Ba-Ba";
	}

	@Override
	public String getDesc() {
		return "Defeat Ba-Ba in a group of two or more, without anyone taking any damage from the following:" +
			" Ba-Ba's Attacks off-prayer, Ba-Ba's slam, rolling boulders, rubble attack or falling rocks. You must have all Ba-Ba invocations activated.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
