package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class UnrequiredAntifire extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Unrequired Antifire";
	}

	@Override
	public String getDesc() {
		return "Kill Cerberus without taking damage from any lava pools.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}

}
