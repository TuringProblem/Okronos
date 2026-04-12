package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class NoSkippingAllowed extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "No Skipping Allowed";
	}

	@Override
	public String getDesc() {
		return "Defeat Ba-Ba after only attacking the non-weakened boulders in the rolling boulder phase, without dying yourself. The Boulderdash invocation must be activated.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
