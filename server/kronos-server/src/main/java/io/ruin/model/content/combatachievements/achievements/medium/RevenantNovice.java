package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class RevenantNovice extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.revenantKills.getKills() >= 25) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Revenant Novice";
	}

	@Override
	public String getDesc() {
		return "Kill 25 Revenants.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}
}
