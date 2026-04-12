package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class NoTimeForDeath extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "No Time For Death";
	}

	@Override
	public String getDesc() {
		return "Finish a Chambers of Xeric raid without killing any Deathly Magers or Deathly Rangers.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
