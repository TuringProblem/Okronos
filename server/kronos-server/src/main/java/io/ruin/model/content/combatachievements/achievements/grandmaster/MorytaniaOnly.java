package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class MorytaniaOnly extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Morytania Only";
	}

	@Override
	public String getDesc() {
		return "Complete the Theatre of Blood without any member of the team equipping a non-barrows weapon (except Dawnbringer).";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
