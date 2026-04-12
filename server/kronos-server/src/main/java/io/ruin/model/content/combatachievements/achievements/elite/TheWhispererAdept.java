package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TheWhispererAdept extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "The Whisperer Adept";
	}

	@Override
	public String getDesc() {
		return "Kill The Whisperer once.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
