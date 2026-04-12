package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class DeathToTheArcherKing extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Death to the Archer King";
	}

	@Override
	public String getDesc() {
		return "Kill Dagannoth Supreme whilst under attack by Dagannoth Prime and Dagannoth Rex.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
