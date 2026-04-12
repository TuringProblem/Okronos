package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.player.Player;

public class FeelingLucky extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Feeling Lucky";
	}

	@Override
	public String getDesc() {
		return "Get a hard clue scroll through pvm.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}
}
