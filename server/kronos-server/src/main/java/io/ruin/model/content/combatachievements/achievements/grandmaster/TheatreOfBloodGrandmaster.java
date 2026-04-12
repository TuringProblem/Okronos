package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TheatreOfBloodGrandmaster extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.theatreOfBloodKills.getKills() >= 300) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Theatre of Blood Grandmaster";
	}

	@Override
	public String getDesc() {
		return "Complete the Theatre of Blood 300 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
