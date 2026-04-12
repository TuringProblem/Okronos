package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class CerberusChampion extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.cerberusKills.getKills() >= 50) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Cerberus Champion";
	}

	@Override
	public String getDesc() {
		return "Kill Cerberus 50 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
