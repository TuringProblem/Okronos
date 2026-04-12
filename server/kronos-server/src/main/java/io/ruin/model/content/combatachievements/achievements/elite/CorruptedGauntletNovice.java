package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class CorruptedGauntletNovice extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.theCorruptedGauntletKills.getKills() >= 5) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Corrupted Gauntlet Novice";
	}

	@Override
	public String getDesc() {
		return "Complete the Corrupted Gauntlet 5 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
