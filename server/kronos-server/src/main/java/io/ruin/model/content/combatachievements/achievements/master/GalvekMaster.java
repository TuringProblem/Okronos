package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class GalvekMaster extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.galvekKills.getKills() >= 100) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Galvek Master";
	}

	@Override
	public String getDesc() {
		return "Kill Galvek 100 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
