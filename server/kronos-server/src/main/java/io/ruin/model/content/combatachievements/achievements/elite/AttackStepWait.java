package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class AttackStepWait extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Attack, Step, Wait";
	}

	@Override
	public String getDesc() {
		return "Survive Verzik Vitur's second phase in the Theatre of Blood without anyone getting bounced by Verzik.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
