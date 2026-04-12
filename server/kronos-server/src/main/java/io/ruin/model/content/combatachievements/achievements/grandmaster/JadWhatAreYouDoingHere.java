package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class JadWhatAreYouDoingHere extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Jad? What Are You Doing Here?";
	}

	@Override
	public String getDesc() {
		return "Kill Tzkal-Zuk without killing the JalTok-Jad which spawns during wave 69.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
