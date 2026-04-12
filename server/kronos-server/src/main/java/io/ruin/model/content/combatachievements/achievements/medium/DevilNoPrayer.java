package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class DevilNoPrayer extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Devil No Prayer";
	}

	@Override
	public String getDesc() {
		return "Kill a Thermonuclear Smoke Devil whilst having no more than 0 prayer points for the entire fight.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}

}
