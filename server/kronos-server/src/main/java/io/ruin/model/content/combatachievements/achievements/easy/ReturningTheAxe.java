package io.ruin.model.content.combatachievements.achievements.easy;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ReturningTheAxe extends CombatAchievement {
	@Override
	public void check(Player player) {

	}

	@Override
	public String getName() {
		return "Returning the axe.";
	}

	@Override
	public String getDesc() {
		return "Kill a Kurask with a leaf-bladed battleaxe.";
	}

	@Override
	public Tier getTier() {
		return Tier.EASY;
	}

}
