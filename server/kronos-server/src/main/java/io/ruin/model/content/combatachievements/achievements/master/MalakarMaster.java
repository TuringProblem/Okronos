package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class MalakarMaster extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.malakarKills.getKills() >= 100) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Malakar Master";
	}

	@Override
	public String getDesc() {
		return "Kill Malakar 100 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
