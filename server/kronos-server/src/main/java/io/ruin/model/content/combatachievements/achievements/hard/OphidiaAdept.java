package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class OphidiaAdept extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Ophidia Adept";
	}

	@Override
	public String getDesc() {
		return "Kill Ophidia once.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
