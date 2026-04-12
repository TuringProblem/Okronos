package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class GauntletNovice extends CombatAchievement {
	@Override
	public void check(Player player) {
		int kc = player.theGauntletKills.getKills() + player.theCorruptedGauntletKills.getKills();
		if (!completed && kc >= 10) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Gauntlet Novice";
	}

	@Override
	public String getDesc() {
		return "Complete the Gauntlet 10 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}

}
