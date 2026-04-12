package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class Hoarder extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Hoarder";
	}

	@Override
	public String getDesc() {
		return "Kill the Chaos Elemental without it unequipping any of your items.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
