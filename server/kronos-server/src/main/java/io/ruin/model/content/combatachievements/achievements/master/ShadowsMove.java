package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ShadowsMove extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Shadows Move...";
	}

	@Override
	public String getDesc() {
		return "Kill Nex without anyone being hit by the Shadow Smash attack.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
