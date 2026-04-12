package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class HealingWontSaveYou extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Healing Won't Save You";
	}

	@Override
	public String getDesc() {
		return "Kill Ophidia whilst 2 or more healing snakes are active.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
