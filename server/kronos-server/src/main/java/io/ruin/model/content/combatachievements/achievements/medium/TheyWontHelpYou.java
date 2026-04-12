package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TheyWontHelpYou extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "They Won't Help You";
	}

	@Override
	public String getDesc() {
		return "Kill Skotizo without killing any of the altars.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}

}
