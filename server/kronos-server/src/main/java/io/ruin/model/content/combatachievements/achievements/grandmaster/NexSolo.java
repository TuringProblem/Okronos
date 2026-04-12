package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class NexSolo extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Nex Solo";
	}

	@Override
	public String getDesc() {
		return "Kill Nex with one or less players at the start of the fight.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
