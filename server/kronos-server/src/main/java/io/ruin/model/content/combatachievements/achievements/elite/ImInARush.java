package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ImInARush extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "I'm in a rush";
	}

	@Override
	public String getDesc() {
		return "Defeat Ba-Ba after destroying four or fewer rolling boulders in total without dying yourself.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
