package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class DagannothSupremeChampion extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.dagannothSupremeKills.getKills() >= 100) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Dagannoth Supreme Champion";
	}

	@Override
	public String getDesc() {
		return "Kill the Dagannoth Supreme 100 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
