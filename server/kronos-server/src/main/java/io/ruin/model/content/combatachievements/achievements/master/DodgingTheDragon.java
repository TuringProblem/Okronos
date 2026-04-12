package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class DodgingTheDragon extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Dodging The Dragon";
	}

	@Override
	public String getDesc() {
		return "Kill Vorkath 5 times without taking any damage from his special attacks and without leaving his area.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
