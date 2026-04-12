package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class SolHereditMaster extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.solHereditKills.getKills() >= 100) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Sol Heredit Master";
	}

	@Override
	public String getDesc() {
		return "Kill Sol Heredit 100 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
