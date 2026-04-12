package io.ruin.model.content.combatachievements.achievements.easy;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class BloodveldVeteran extends CombatAchievement {

	@Override
	public void check(Player player) {
		if (player.bloodveldKills.getKills() >= 50 && !completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Bloodveld Veteran";
	}

	@Override
	public String getDesc() {
		return "Kill 50 Bloodvelds.";
	}

	@Override
	public Tier getTier() {
		return Tier.EASY;
	}

}
