package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.player.Player;

public class KingBlackDragonChampion extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.kingBlackDragonKills.getKills() >= 25) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "King Black Dragon Champion";
	}

	@Override
	public String getDesc() {
		return "Kill the King Black Dragon 25 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}

}
