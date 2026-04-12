package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class RespiratoryRunner extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}


	@Override
	public String getName() {
		return "Respiratory Runner";
	}

	@Override
	public String getDesc() {
		return "Kill the Abyssal Sire after only stunning him once.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}

}
