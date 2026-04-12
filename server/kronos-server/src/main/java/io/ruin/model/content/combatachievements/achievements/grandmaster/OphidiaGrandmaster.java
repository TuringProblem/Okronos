package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class OphidiaGrandmaster extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.ophidiaKills.getKills() >= 250) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Ophidia Grandmaster";
	}

	@Override
	public String getDesc() {
		return "Kill Ophidia 250 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
