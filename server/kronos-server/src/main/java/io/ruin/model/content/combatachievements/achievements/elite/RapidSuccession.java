package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class RapidSuccession extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Rapid Succession";
	}

	@Override
	public String getDesc() {
		return "Kill all three Dagannoth Kings within 9 seconds of the first one.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
