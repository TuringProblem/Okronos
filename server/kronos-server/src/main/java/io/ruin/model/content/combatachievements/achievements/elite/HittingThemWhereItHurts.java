package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.combat.Combat;
import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class HittingThemWhereItHurts extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Hitting Them Where It Hurts";
	}

	@Override
	public String getDesc() {
		return "Finish off a Demonic Gorilla with a demonbane weapon.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
