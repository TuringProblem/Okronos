package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerfectAkkha extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perfect Akkha";
	}

	@Override
	public String getDesc() {
		return "Complete Akkha in a group of two or more, without anyone taking any damage from the following: Akkha's attacks off-prayer" +
			", Akkha's special attacks (orbs, memory, detonate), exploding shadow timers, orbs in the enrage phase or attacking Akkha with the wrong style. You must have all Akkha invocations activated.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
