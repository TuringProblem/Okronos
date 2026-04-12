package io.ruin.model.content.combatachievements.achievements.easy;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class BlackDragonAdept extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Black Dragon Adept";
	}

	@Override
	public String getDesc() {
		return "Kill a black dragon.";
	}

	@Override
	public Tier getTier() {
		return Tier.EASY;
	}

}
