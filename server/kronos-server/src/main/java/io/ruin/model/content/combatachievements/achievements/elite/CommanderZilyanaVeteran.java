package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class CommanderZilyanaVeteran extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.commanderZilyanaKills.getKills() >= 150) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Commander Zilyana Veteran";
	}

	@Override
	public String getDesc() {
		return "Kill Commander Zilyana 150 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
