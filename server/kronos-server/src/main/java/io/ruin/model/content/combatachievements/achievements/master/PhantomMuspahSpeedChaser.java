package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PhantomMuspahSpeedChaser extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Phantom Muspah Speed-Chaser";
	}

	@Override
	public String getDesc() {
		return "Kill the Phantom Muspah in under 45 seconds without a slayer task.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
