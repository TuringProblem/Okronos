package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerfectXarpus extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perfect Xarpus";
	}

	@Override
	public String getDesc() {
		return "Kill Xarpus without anyone in the team taking any damage from Xarpus' attacks.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
