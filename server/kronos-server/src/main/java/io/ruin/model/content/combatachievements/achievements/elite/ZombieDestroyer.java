package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ZombieDestroyer extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Zombie Destroyer";
	}

	@Override
	public String getDesc() {
		return "Kill Vorkath's zombified spawn without using crumble undead.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
