package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TakeThat extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Take That!";
	}

	@Override
	public String getDesc() {
		return "Kill a snakeling with a ring of recoil.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
