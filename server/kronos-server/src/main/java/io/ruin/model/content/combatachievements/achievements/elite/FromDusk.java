package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class FromDusk extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "From Dusk...";
	}

	@Override
	public String getDesc() {
		return "Kill the Grotesque Guardians 10 times without leaving the instance.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
