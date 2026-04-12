package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class CollateralDamage extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Collateral Damage";
	}

	@Override
	public String getDesc() {
		return "Kill Kree'arra in a private instance without ever attacking him directly.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
