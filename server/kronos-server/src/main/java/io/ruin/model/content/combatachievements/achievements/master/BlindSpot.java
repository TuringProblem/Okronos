package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class BlindSpot extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Blind Spot";
	}

	@Override
	public String getDesc() {
		return "Kill Tekton without taking any damage.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
