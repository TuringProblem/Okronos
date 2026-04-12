package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class NoPillar extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "No-Pillar";
	}

	@Override
	public String getDesc() {
		return "Survive Verzik Vitur's pillar phase in the Theatre of Blood: Entry Mode without losing a single pillar.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
