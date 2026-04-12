package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class GauntletMaster extends CombatAchievement {
	@Override
	public void check(Player player) {
		int kc = player.theCorruptedGauntletKills.getKills() + player.theGauntletKills.getKills();
		if (!completed && kc >= 100) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Gauntlet Master";
	}

	@Override
	public String getDesc() {
		return "Complete the Gauntlet 100 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
