package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TheatreOfBloodNovice extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.theatreOfBloodKills.getKills() >= 25) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Theatre of Blood Novice";
	}

	@Override
	public String getDesc() {
		return "Complete the Theatre of Blood 25 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
