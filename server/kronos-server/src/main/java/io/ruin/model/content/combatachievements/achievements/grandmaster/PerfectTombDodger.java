package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerfectTombDodger extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perfect Tomb Dodger";
	}

	@Override
	public String getDesc() {
		return "Complete 'Perfect Baba' and 'Perfect Kephri' in a single run of the Tombs of Amascut.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
