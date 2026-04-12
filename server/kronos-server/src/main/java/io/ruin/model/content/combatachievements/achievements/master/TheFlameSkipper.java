package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TheFlameSkipper extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "The Flame Skipper";
	}

	@Override
	public String getDesc() {
		return "Kill the Alchemical Hydra without letting it spawn a flame wall attack.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
