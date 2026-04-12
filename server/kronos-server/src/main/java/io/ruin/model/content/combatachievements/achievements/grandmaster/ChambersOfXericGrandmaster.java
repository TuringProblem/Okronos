package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ChambersOfXericGrandmaster extends CombatAchievement {
	@Override
	public void check(Player player) {
		int coxKc = player.chambersofXericKills.getKills() + player.olmOnlyKills.getKills();
		if (!completed && coxKc >= 300) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Chambers of Xeric Grandmaster";
	}

	@Override
	public String getDesc() {
		return "Complete the Chambers of Xeric 300 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
