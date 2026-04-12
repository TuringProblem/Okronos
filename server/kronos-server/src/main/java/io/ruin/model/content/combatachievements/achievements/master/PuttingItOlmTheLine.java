package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PuttingItOlmTheLine extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Putting It Olm The Line";
	}

	@Override
	public String getDesc() {
		return "Complete a Chambers of Xeric solo raid with more than 80,000 points.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
