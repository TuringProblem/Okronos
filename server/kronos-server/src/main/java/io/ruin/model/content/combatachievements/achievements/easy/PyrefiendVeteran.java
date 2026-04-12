package io.ruin.model.content.combatachievements.achievements.easy;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PyrefiendVeteran extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (player.pyrefiendKills.getKills() >= 75 && !completed) {
			completed = true;
			complete(player);
		}

	}

	@Override
	public String getName() {
		return "Pyrefiend Veteran";
	}

	@Override
	public String getDesc() {
		return "Kill 75 pyrefiends.";
	}

	@Override
	public Tier getTier() {
		return Tier.EASY;
	}

}
