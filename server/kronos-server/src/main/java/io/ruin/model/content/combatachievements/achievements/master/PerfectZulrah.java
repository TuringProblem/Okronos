package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerfectZulrah extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perfect Zulrah";
	}

	@Override
	public String getDesc() {
		return "Kill Zulrah whilst taking no damage from the following: Venom Clouds, Zulrah's Green or Crimson phase.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
