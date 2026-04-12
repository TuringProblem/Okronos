package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class DownDoSpecs extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Down Do Specs";
	}

	@Override
	public String getDesc() {
		return "Defeat the Wardens after staggering the boss a maximum of twice during phase two, without dying yourself.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
