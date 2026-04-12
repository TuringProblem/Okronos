package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.combat.Combat;
import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class EgniolDietII extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Egniol Diet II";
	}

	@Override
	public String getDesc() {
		return "Kill the Corrupted Hunllef without making an egniol potion within the Corrupted Gauntlet.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
