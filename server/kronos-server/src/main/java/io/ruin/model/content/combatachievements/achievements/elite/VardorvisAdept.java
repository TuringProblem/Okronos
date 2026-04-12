package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class VardorvisAdept extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Vardorvis Adept";
	}

	@Override
	public String getDesc() {
		return "Kill Vardorvis once.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
