package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.combat.Combat;
import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class StickEmWithThePointyEnd extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Stick 'em With the Pointy End";
	}

	@Override
	public String getDesc() {
		return "Kill Vorkath using melee weapons only.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
