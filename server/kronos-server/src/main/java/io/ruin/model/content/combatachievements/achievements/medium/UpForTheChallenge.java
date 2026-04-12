package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class UpForTheChallenge extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Up For The Challenge";
	}

	@Override
	public String getDesc() {
		return "Kill Skotizo without equipping a demonbane weapon.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}

}
