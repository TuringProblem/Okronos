package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class GiantMoleChampion extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.giantMoleKills.getKills() >= 25) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Giant Mole Champion";
	}

	@Override
	public String getDesc() {
		return "Kill the Giant Mole 25 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}
}
