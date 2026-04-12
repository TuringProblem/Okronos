package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.player.Player;

public class CommanderShowdown extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Commander Showdown";
	}

	@Override
	public String getDesc() {
		return "Finish off Commander Zilyana while all of her bodyguards are dead.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
