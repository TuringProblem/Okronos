package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class GeneralGraardorVeteran extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.generalGraardorKills.getKills() >= 150) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "General Graardor Veteran";
	}

	@Override
	public String getDesc() {
		return "Kill General Graardor 150 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
