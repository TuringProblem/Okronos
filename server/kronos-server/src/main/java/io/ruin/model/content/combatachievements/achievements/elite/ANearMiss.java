package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ANearMiss extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "A Near Miss!";
	}

	@Override
	public String getDesc() {
		return "Complete the Fight Caves after surviving a hit from TzTok-Jad without praying.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
