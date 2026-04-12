package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class CorruptedGauntletGrandmaster extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.theCorruptedGauntletKills.getKills() >= 100) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Corrupted Gauntlet Grandmaster";
	}

	@Override
	public String getDesc() {
		return "Complete the Corrupted Gauntlet 100 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
