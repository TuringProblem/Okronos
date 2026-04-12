package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class UndyingRaider extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Undying Raider";
	}

	@Override
	public String getDesc() {
		return "Complete a Chambers of Xeric solo raid without dying.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
