package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerfectSire extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perfect Sire";
	}

	@Override
	public String getDesc() {
		return "Kill the Abyssal Sire without taking damage from the external tentacles, miasma pools, explosion or damage from the Abyssal Sire without praying the appropriate protection prayer.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}

}
