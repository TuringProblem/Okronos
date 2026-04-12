package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ChaosFanaticAdept extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Chaos Fanatic Adept";
	}

	@Override
	public String getDesc() {
		return "Kill the Chaos Fanatic once.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}

}
