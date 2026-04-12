package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class MovinOnUp extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Movin' on up";
	}

	@Override
	public String getDesc() {
		return "Complete a Tombs of Amascut raid at level 50 or above.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
