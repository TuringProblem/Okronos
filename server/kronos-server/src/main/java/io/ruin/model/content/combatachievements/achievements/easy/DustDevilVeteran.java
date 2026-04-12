package io.ruin.model.content.combatachievements.achievements.easy;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class DustDevilVeteran extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.dustDevilKills.getKills() >= 50) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Dust Devil Veteran";
	}

	@Override
	public String getDesc() {
		return "Kill 50 Dust Devils.";
	}

	@Override
	public Tier getTier() {
		return Tier.EASY;
	}

}
