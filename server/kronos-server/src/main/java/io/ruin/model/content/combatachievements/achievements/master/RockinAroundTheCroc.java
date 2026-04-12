package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class RockinAroundTheCroc extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Rockin' around the croc";
	}

	@Override
	public String getDesc() {
		return "Defeat Zebak with all Zebak invocations activated and the path levelled up to at least four, without dying yourself.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
