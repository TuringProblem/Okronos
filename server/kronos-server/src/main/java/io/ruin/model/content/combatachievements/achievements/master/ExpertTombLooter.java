package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ExpertTombLooter extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.tombsOfAmascutExpertKills.getKills() >= 75) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Expert Tomb Looter";
	}

	@Override
	public String getDesc() {
		return "Complete the Tombs of Amascut (Expert mode) 75 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
