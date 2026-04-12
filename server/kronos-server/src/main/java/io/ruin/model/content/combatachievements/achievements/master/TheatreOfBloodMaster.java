package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TheatreOfBloodMaster extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.theatreOfBloodKills.getKills() >= 150) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Theatre of Blood Master";
	}

	@Override
	public String getDesc() {
		return "Complete the Theatre of Blood 150 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
