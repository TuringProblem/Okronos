package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class AbyssalSireChampion extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.abyssalSireKills.getKills() >= 50) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Abyssal Sire Champion";
	}

	@Override
	public String getDesc() {
		return "Kill the Abyssal Sire 50 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
