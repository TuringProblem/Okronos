package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ChambersOfXericVeteran extends CombatAchievement {
	@Override
	public void check(Player player) {
		int coxKc = player.chambersofXericKills.getKills() + player.olmOnlyKills.getKills();
		if (coxKc >= 75) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Chambers of Xeric Veteran";
	}

	@Override
	public String getDesc() {
		return "Complete Chambers of Xeric 75 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
