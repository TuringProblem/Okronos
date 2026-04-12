package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class FancyFeet extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Fancy Feet";
	}

	@Override
	public String getDesc() {
		return "Complete the final phase of The Wardens in a group of two or more, using only melee attacks and without dying yourself. The 'Insanity' invocation must be activated.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
