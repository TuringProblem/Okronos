package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class KreeArraAdept extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Kree'arra Adept";
	}

	@Override
	public String getDesc() {
		return "Kill Kree'arra once.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}
}
