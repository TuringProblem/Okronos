package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class DemonicWeakening extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Demonic Weakening";
	}

	@Override
	public String getDesc() {
		return "Kill Skotizo with no altars active.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}

}
