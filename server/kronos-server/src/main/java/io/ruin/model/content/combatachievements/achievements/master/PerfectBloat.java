package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerfectBloat extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perfect Bloat";
	}

	@Override
	public String getDesc() {
		return "Kill the Pestilent Bloat without anyone in the team taking damage from the following sources: Pestilent flies, Falling body parts or The Pestilent Bloats stomp attack.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
