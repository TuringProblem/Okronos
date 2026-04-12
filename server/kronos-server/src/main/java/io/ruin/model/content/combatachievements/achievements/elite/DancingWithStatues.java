package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class DancingWithStatues extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Dancing with Statues";
	}

	@Override
	public String getDesc() {
		return "Receive kill-credit for a Stone Guardian without taking damage from falling rocks.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
