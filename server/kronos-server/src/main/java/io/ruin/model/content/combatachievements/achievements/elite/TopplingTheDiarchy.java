package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TopplingTheDiarchy extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Toppling the Diarchy";
	}

	@Override
	public String getDesc() {
		return "Kill Dagannoth Rex and one other Dagannoth king at the exact same time.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
