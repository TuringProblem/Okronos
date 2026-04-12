package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PrecisePositioning extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Precise Positioning";
	}

	@Override
	public String getDesc() {
		return "Kill Skotizo with the final source of damage being a Chinchompa explosion.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
