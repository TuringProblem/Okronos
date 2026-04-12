package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ContainThis extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Contain this!";
	}

	@Override
	public String getDesc() {
		return "Kill Nex without anyone taking damage from any Ice special attack.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
