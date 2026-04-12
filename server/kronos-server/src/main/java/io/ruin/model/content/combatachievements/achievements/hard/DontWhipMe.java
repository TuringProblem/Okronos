package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class DontWhipMe extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Don't Whip Me";
	}

	@Override
	public String getDesc() {
		return "Kill the Abyssal Sire without being hit by any external tentacles.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
