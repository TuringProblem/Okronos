package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ExpertTombLooterII extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.tombsOfAmascutExpertKills.getKills() >= 250) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Expert Tomb Looter II";
	}

	@Override
	public String getDesc() {
		return "Complete the Tombs of Amascut (Expert mode) 250 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
