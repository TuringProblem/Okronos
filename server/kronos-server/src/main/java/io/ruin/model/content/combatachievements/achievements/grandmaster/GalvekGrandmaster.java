package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class GalvekGrandmaster extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.galvekKills.getKills() >= 250) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Galvek Grandmaster";
	}

	@Override
	public String getDesc() {
		return "Kill Galvek 250 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
