package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ChitinPenetrator extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Chitin Penetrator";
	}

	@Override
	public String getDesc() {
		return "Kill the Kalphite Queen while her defence is lowered.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
