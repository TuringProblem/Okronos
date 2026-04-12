package io.ruin.model.content.combatachievements.achievements.easy;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class CaveHorrorVeteran extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (player.caveHorrorKills.getKills() >= 50 && !completed) {
			completed = true;
			complete(player);
		}
	}


	@Override
	public String getName() {
		return "Cave Horror Veteran";
	}

	@Override
	public String getDesc() {
		return "Kill 50 Cave Horrors.";
	}

	@Override
	public Tier getTier() {
		return Tier.EASY;
	}

}
