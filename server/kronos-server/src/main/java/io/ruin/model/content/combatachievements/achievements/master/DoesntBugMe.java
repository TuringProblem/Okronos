package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class DoesntBugMe extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Doesn't Bug Me";
	}

	@Override
	public String getDesc() {
		return "Defeat Kephri with all Kephri invocations activated and the path levelled up to at least four, without dying yourself.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
