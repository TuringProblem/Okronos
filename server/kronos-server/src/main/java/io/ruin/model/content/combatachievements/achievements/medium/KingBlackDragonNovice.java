package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.player.Player;

public class KingBlackDragonNovice extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.kingBlackDragonKills.getKills() >= 10) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "King Black Dragon Novice";
	}

	@Override
	public String getDesc() {
		return "Kill the King Black Dragon 10 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}

}
