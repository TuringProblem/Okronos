package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PeachConjurer extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Peach Conjurer";
	}

	@Override
	public String getDesc() {
		return "Kill Commander Zilyana 50 times in a privately rented instance without leaving the room.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
