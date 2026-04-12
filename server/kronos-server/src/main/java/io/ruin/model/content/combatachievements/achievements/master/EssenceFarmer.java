package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class EssenceFarmer extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Essence Farmer";
	}

	@Override
	public String getDesc() {
		return "Kill the Phantom Muspah 10 times in one trip.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
