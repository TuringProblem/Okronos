package io.ruin.model.content.combatachievements.achievements.easy;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class WyrmVeteran extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.wyrmKills.getKills() >= 50) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Wyrm Veteran";
	}

	@Override
	public String getDesc() {
		return "Kill 50 wyrms.";
	}

	@Override
	public Tier getTier() {
		return Tier.EASY;
	}

}
