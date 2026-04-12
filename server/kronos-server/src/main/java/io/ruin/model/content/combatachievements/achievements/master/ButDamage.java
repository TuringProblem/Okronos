package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ButDamage extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "But... Damage";
	}

	@Override
	public String getDesc() {
		return "Complete an expert run of Tombs of Amascut without anyone in your party wearing or holding any equipment at tier 75 or above.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
