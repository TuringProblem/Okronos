package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class DagannothPrimeChampion extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.dagannothPrimeKills.getKills() >= 100) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Dagannoth Prime Champion";
	}

	@Override
	public String getDesc() {
		return "Kill the Dagannoth Prime 100 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
