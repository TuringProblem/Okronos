package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerfectGalvek extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perfect Galvek";
	}

	@Override
	public String getDesc() {
		return "Kill Galvek without taking unavoidable damage from the sources: Auto attacks off prayer, Waves, Fireballs, Spheres, Bomb clusters, Portal attacks.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
