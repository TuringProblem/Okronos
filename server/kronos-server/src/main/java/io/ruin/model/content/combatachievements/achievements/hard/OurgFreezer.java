package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class OurgFreezer extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Ourg Freezer";
	}

	@Override
	public String getDesc() {
		return "Kill the General Graardor whilst he is frozen.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
