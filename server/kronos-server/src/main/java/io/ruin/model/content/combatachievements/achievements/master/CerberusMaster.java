package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class CerberusMaster extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.cerberusKills.getKills() >= 300) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Cerberus Master";
	}

	@Override
	public String getDesc() {
		return "Kill Cerberus 300 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
