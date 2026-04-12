package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerfectTombsOfAmascut extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perfect Tombs of Amascut";
	}

	@Override
	public String getDesc() {
		return "Complete 'Perfect Akkha', 'Perfect Zebak', 'Perfect Baba', 'Perfect Kephri' and 'Perfect Wardens' in a single run of the Tombs of Amascut.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
