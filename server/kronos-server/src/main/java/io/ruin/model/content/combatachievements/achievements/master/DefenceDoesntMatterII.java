package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class DefenceDoesntMatterII extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Defence Doesn't Matter II";
	}

	@Override
	public String getDesc() {
		return "Kill the Corrupted Hunllef without making any armour within the Corrupted Gauntlet.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
