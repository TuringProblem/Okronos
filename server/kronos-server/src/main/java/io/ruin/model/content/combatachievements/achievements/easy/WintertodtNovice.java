package io.ruin.model.content.combatachievements.achievements.easy;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class WintertodtNovice extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.wintertodtKills.getKills() >= 10) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Wintertodt Novice";
	}

	@Override
	public String getDesc() {
		return "Kill Wintertodt 10 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.EASY;
	}

}
