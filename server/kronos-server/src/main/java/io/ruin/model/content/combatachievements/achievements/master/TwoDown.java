package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TwoDown extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Two Down";
	}

	@Override
	public String getDesc() {
		return "Kill the Pestilent Bloat before he shuts down for the third time.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
