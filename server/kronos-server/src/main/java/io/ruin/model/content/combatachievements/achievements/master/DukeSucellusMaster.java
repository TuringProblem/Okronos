package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class DukeSucellusMaster extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.dukeKills.getKills() >= 150) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Duke Sucellus Master";
	}

	@Override
	public String getDesc() {
		return "Kill Duke Sucellus 150 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
