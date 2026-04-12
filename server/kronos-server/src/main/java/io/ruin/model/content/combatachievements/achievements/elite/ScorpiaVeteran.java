package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ScorpiaVeteran extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.scorpiaKills.getKills() >= 150) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Scorpia Veteran";
	}

	@Override
	public String getDesc() {
		return "Kill Scorpia 150 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
