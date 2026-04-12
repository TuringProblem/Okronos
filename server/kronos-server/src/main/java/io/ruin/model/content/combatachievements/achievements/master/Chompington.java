package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class Chompington extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Chompington";
	}

	@Override
	public String getDesc() {
		return "Defeat Zebak using only melee attacks and without dying yourself.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
