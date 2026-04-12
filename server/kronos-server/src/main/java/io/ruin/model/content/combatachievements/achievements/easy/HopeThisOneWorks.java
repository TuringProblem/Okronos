package io.ruin.model.content.combatachievements.achievements.easy;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class HopeThisOneWorks extends CombatAchievement {

	@Override
	public void check(Player player) {

	}

	@Override
	public String getName() {
		return "Hope this one works";
	}

	@Override
	public String getDesc() {
		return "Kill a turoth with a leaf-bladed sword.";
	}

	@Override
	public Tier getTier() {
		return Tier.EASY;
	}

}
