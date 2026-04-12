package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class GeneralGraardorChampion extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.generalGraardorKills.getKills() >= 50) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "General Graardor Champion";
	}

	@Override
	public String getDesc() {
		return "Kill the General Graardor 50 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
