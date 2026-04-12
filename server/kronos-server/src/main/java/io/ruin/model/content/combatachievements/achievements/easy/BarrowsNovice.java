package io.ruin.model.content.combatachievements.achievements.easy;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class BarrowsNovice extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.barrowsChestsOpened >= 10) {
			completed = true;
			complete(player);
		}
	}


	@Override
	public String getName() {
		return "Barrows Novice";
	}

	@Override
	public String getDesc() {
		return "Loot 10 Barrows chests.";
	}

	@Override
	public Tier getTier() {
		return Tier.EASY;
	}

}
