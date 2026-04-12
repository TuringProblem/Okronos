package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class DukeSucellusSpeedChaser extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Duke Sucellus Speed-Chaser";
	}

	@Override
	public String getDesc() {
		return "Kill Duke Sucellus in less than 40 seconds without a slayer task.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
