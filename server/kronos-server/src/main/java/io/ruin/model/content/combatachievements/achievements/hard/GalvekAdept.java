package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class GalvekAdept extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Galvek Adept";
	}

	@Override
	public String getDesc() {
		return "Kill Galvek once.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
