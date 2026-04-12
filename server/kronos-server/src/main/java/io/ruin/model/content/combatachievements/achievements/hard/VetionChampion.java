package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class VetionChampion extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.vetionKills.getKills() >= 50) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Vet'ion Champion";
	}

	@Override
	public String getDesc() {
		return "Kill Vet'ion 50 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}
}
