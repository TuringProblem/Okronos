package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class NexDuo extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Nex Duo";
	}

	@Override
	public String getDesc() {
		return "Kill Nex with two or less players at the start of the fight.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
