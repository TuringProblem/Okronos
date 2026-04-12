package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class CorruptedHunllefSpeedRunner extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Corrupted Hunllef Speed-Runner";
	}

	@Override
	public String getDesc() {
		return "Complete the Corrupted Gauntlet in under 2 minutes.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
