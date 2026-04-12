package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PhantomMuspahSpeedRunner extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Phantom Muspah Speed-Runner";
	}

	@Override
	public String getDesc() {
		return "Kill the Phantom Muspah in under 35 seconds without a slayer task.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
