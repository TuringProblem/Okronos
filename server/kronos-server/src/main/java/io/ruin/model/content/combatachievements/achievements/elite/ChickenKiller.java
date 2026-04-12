package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ChickenKiller extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Chicken Killer";
	}

	@Override
	public String getDesc() {
		return "Kill the Corporeal Beast solo.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
