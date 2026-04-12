package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ChaosFanaticChampion extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.chaosFanaticKills.getKills() >= 50) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Chaos Fanatic Champion";
	}

	@Override
	public String getDesc() {
		return "Kill the Chaos Fanatic 50 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}
}
