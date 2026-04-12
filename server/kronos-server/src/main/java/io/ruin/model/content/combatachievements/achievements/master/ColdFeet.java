package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ColdFeet extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Cold Feet";
	}

	@Override
	public String getDesc() {
		return "Kill Duke Sucellus without taking any avoidable damage, whilst also never running.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
