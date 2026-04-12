package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class VorkathMaster extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.vorkathKills.getKills() >= 250) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Vorkath Master";
	}

	@Override
	public String getDesc() {
		return "Kill Vorkath 250 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
