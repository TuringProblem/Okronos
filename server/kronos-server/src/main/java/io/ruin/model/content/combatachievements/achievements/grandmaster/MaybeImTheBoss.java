package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class MaybeImTheBoss extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Maybe I'm The Boss";
	}

	@Override
	public String getDesc() {
		return "Complete a Tombs of Amascut raid with every single boss invocation activated and without anyone dying.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
