package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class AbyssalSireVeteran extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.abyssalSireKills.getKills() >= 150) {
			completed = true;
			complete(player);
		}
	}


	@Override
	public String getName() {
		return "Abyssal Sire Veteran";
	}

	@Override
	public String getDesc() {
		return "Kill the Abyssal Sire 150 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}


}
