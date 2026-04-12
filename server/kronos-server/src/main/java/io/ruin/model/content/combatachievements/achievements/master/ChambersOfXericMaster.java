package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ChambersOfXericMaster extends CombatAchievement {
	@Override
	public void check(Player player) {
		int coxKc = player.olmOnlyKills.getKills() + player.chambersofXericKills.getKills();
		if (coxKc >= 150) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Chambers of Xeric Master";
	}

	@Override
	public String getDesc() {
		return "Complete the Chambers of Xeric 150 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
