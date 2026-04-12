package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.combat.Combat;
import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class Anticoagulants extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Anticoagulants";
	}

	@Override
	public String getDesc() {
		return "Defeat the Maiden of Sugadinti in the Theatre of Blood without letting any bloodspawn live for longer than 10 seconds.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
