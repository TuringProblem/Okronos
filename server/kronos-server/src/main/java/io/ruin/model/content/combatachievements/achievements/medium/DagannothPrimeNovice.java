package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class DagannothPrimeNovice extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.dagannothPrimeKills.getKills() >= 10) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Dagannoth Prime Novice";
	}

	@Override
	public String getDesc() {
		return "Kill the Dagannoth Prime 10 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}

}
