package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class VetionVeteran extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.vetionKills.getKills() >= 150) {
			completed = true;
			complete(player);
		}
	}


	@Override
	public String getName() {
		return "Vet'ion Veteran";
	}

	@Override
	public String getDesc() {
		return "Kill Vet'ion 150 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}

}
