package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class WolfPuncherII extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Wolf Puncher II";
	}

	@Override
	public String getDesc() {
		return "Kill the Corrupted Hunllef without making more than one attuned weapon.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
