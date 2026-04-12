package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class AirborneShowdown extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Airborne Showdown";
	}

	@Override
	public String getDesc() {
		return "Finish off Kree'arra whilst all of his bodyguards are dead.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
