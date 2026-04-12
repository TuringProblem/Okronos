package io.ruin.model.content.combatachievements.achievements.easy;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class WintertodtAdept extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.wintertodtKills.getKills() >= 1) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Wintertodt Adept";
	}

	@Override
	public String getDesc() {
		return "Kill Wintertodt once.";
	}

	@Override
	public Tier getTier() {
		return Tier.EASY;
	}

}
