package io.ruin.model.content.combatachievements.achievements.easy;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class FireGiantAdept extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Fire Giant Adept";
	}

	@Override
	public String getDesc() {
		return "Kill a fire giant.";
	}

	@Override
	public Tier getTier() {
		return Tier.EASY;
	}

}
