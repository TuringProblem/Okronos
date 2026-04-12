package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ClawClipper extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Claw Clipper";
	}

	@Override
	public String getDesc() {
		return "Kill the King Black Dragon with protect from melee active.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}

}
