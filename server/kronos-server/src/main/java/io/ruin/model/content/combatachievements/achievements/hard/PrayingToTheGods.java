package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PrayingToTheGods extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.chaosFanaticKillsSincePotion >= 10) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Praying to the Gods";
	}

	@Override
	public String getDesc() {
		return "Kill the Chaos Fanatic 10 times without drinking any potion which restores prayer or leaving the Wilderness.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}
}
