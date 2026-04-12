package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class CorruptedGauntletMaster extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.theCorruptedGauntletKills.getKills() >= 75) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Corrupted Gauntlet Master";
	}

	@Override
	public String getDesc() {
		return "Complete the Corrupted Gauntlet 75 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
