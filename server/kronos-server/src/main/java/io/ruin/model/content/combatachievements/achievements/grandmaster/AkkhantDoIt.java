package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class AkkhantDoIt extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Akkhan't Do It";
	}

	@Override
	public String getDesc() {
		return "Defeat Akkha with all Akkha invocations activated and the path levelled up to at least four, without dying yourself.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
