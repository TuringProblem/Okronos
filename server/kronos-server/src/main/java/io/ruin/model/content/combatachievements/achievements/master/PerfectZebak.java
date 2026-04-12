package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerfectZebak extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perfect Zebak";
	}

	@Override
	public String getDesc() {
		return "Defeat Zebak without anyone taking any damage from: poison, Zebak's basic attacks off-prayer, blood spawns and waves." +
			" You also must not push more than two jugs on the roar attack during the fight (you may destroy stationary ones). You must have all Zebak invocations activated.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
