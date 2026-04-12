package io.ruin.model.content.combatachievements.achievements.easy;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class AberrantSpectreVeteran extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.aberrantSpectreKills.getKills() >= 50) {
			completed = true;
			complete(player);
		}
	}


	@Override
	public String getName() {
		return "Aberrant Spectre Veteran";
	}

	@Override
	public String getDesc() {
		return "Kill 50 Aberrant Spectres";
	}

	@Override
	public Tier getTier() {
		return Tier.EASY;
	}

}
