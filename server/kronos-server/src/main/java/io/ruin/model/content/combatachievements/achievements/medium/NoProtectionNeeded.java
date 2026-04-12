package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class NoProtectionNeeded extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "No Protection Needed";
	}

	@Override
	public String getDesc() {
		return "Kill the King Black Dragon without using any protection prayers.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}

}
