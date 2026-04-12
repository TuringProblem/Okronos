package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class FaithlessCryptRun extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Faithless Crypt Run";
	}

	@Override
	public String getDesc() {
		return "Kill all six Barrows Brothers and loot the Barrows chest without ever having more than 0 prayer points.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
