package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class AFrozenKing extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "A Frozen King";
	}

	@Override
	public String getDesc() {
		return "Kill the Dagannoth Rex whilst he's frozen.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}
}
