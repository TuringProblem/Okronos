package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.combat.Combat;
import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class OneHundredTentacles extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "One Hundred Tentacles";
	}

	@Override
	public String getDesc() {
		return "Kill the Kraken 100 times in a private instance without leaving the room.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
