package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class WolfPuncher extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Wolf Puncher";
	}

	@Override
	public String getDesc() {
		return "Kill the Crystalline Hunllef without making more than one attuned weapon.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
