package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerfectVerzik extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perfect Verzik";
	}

	@Override
	public String getDesc() {
		return "Defeat Verzik Vitur without anyone in the team taking damage from Verzik Vitur's attacks other than her spider form's correctly prayed against regular magical and ranged attacks.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
