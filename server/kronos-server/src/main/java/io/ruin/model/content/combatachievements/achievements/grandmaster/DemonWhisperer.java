package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class DemonWhisperer extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Demon Whisperer";
	}

	@Override
	public String getDesc() {
		return "Kill K'ril Tsutsaroth in a private instance without ever being hit by his bodyguards.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
