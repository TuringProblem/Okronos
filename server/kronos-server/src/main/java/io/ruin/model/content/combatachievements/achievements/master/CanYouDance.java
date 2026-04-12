package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class CanYouDance extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Can You Dance?";
	}

	@Override
	public String getDesc() {
		return "Kill Xarpus without anyone in the team using a ranged or magic weapon.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
