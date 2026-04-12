package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class CerberusVeteran extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.cerberusKills.getKills() >= 150) {
			completed = true;
			complete(player);
		}
	}


	@Override
	public String getName() {
		return "Cerberus Veteran";
	}

	@Override
	public String getDesc() {
		return "Kill Cerberus 150 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}

}
