package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class DagannothRexNovice extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.dagannothRexKills.getKills() >= 10) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Dagannoth Rex Novice";
	}

	@Override
	public String getDesc() {
		return "Kill the Dagannoth Rex 10 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}

}
