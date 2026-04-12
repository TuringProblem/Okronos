package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class WalkStraightPrayTrue extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Walk Straight Pray True";
	}

	@Override
	public String getDesc() {
		return "Kill the Phantom Muspah without taking any avoidable damage.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
