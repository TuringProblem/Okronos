package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class DagannothSupremeNovice extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.dagannothSupremeKills.getKills() >= 10) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Dagannoth Supreme Novice";
	}

	@Override
	public String getDesc() {
		return "Kill Dagannoth Supreme 10 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}

}
