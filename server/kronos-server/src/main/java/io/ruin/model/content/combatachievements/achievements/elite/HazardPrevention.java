package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class HazardPrevention extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Hazard Prevention";
	}

	@Override
	public String getDesc() {
		return "Kill the Thermonuclear Smoke Devil without it hitting anyone.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
