package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class Insanity extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Insanity";
	}

	@Override
	public String getDesc() {
		return "Complete 'Perfect Wardens' at 500 raid invocation level or above.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
