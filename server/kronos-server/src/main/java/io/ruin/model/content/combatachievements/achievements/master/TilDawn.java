package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TilDawn extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "... 'til Dawn";
	}

	@Override
	public String getDesc() {
		return "Kill the Grotesque Guardians 20 times without leaving the instance.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
