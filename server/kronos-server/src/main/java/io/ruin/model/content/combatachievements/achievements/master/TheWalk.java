package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TheWalk extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "The Walk";
	}

	@Override
	public String getDesc() {
		return "Hit Vorkath 20 times during the acid special without getting hit by his rapid fire or the acid pools.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
