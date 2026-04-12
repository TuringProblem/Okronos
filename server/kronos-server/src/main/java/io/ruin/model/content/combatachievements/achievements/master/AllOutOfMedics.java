package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class AllOutOfMedics extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "All Out of Medics";
	}

	@Override
	public String getDesc() {
		return "Defeat Kephri without letting her heal above 25% after the first down. The 'Medic' invocation must be activated. You must do this without dying yourself.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
