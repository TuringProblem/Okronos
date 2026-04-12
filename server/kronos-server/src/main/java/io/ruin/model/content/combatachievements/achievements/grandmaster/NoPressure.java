package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class NoPressure extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "No Pressure";
	}

	@Override
	public String getDesc() {
		return "Kill the Alchemical Hydra using only Dharok's Greataxe as a weapon whilst having no more than 10 Hitpoints throughout the entire fight.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
