package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class KrilTsutsarothChampion extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.krilTsutsarothKills.getKills() >= 50) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "K'ril Tsutsaroth Champion";
	}

	@Override
	public String getDesc() {
		return "Kill K'ril Tsutsaroth 50 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
