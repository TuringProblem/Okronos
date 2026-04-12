package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ArmouredZombieAdept extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Armoured Zombie Adept";
	}

	@Override
	public String getDesc() {
		return "Kill an Armoured zombie once.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}

}
