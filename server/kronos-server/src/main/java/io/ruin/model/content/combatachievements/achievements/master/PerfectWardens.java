package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerfectWardens extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perfect Wardens";
	}

	@Override
	public String getDesc() {
		return "Defeat The Wardens in a group of two or more, without anyone taking avoidable damage from the following:" +
			" Warden attacks, lightning attacks in phase three, skull attack in phase three, Demi god attacks in phase three. You must have all Wardens invocations activated.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
