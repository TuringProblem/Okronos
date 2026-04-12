package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class VorkathChampion extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.vorkathKills.getKills() >= 75) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Vorkath Champion";
	}

	@Override
	public String getDesc() {
		return "Kill Vorkath 75 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
