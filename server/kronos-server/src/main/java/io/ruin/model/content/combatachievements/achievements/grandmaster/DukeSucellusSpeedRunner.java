package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class DukeSucellusSpeedRunner extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Duke Sucellus Speed-Runner";
	}

	@Override
	public String getDesc() {
		return "Kill Duke Sucellus in under 30 seconds.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
