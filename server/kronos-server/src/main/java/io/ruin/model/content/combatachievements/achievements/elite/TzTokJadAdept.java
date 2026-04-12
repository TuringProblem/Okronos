package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TzTokJadAdept extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "TzTok-Jad Adept";
	}

	@Override
	public String getDesc() {
		return "Defeat TzTok-Jad in the Fight Caves once.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
