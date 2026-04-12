package io.ruin.model.content.combatachievements.achievements.easy;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class DefenceWhatDefence extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}


	@Override
	public String getName() {
		return "Defence? What Defence?";
	}

	@Override
	public String getDesc() {
		return "Kill any Barrows Brother using only magical damage.";
	}

	@Override
	public Tier getTier() {
		return Tier.EASY;
	}

}
