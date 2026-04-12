package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class AnimalWhisperer extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Animal Whisperer";
	}

	@Override
	public String getDesc() {
		return "Kill Commander Zilyana in a private instance without taking any damage from the boss or bodyguards.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
