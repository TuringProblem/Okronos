package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class NexTrio extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Nex Trio";
	}

	@Override
	public String getDesc() {
		return "Kill Nex with three or less players at the start of the fight.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
