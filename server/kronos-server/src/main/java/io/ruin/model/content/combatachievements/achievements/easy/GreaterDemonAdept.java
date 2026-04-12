package io.ruin.model.content.combatachievements.achievements.easy;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class GreaterDemonAdept extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}


	@Override
	public String getName() {
		return "Greater Demon Adept";
	}

	@Override
	public String getDesc() {
		return "Kill a greater demon.";
	}

	@Override
	public Tier getTier() {
		return Tier.EASY;
	}

}
