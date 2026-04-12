package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class MovingCollateral extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Moving Collateral";
	}

	@Override
	public String getDesc() {
		return "Kill Commander Zilyana in a private instance without attacking her directly.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
