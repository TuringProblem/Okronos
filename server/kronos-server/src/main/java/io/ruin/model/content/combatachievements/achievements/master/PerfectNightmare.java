package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerfectNightmare extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perfect Nightmare";
	}

	@Override
	public String getDesc() {
		return "Kill the Nightmare without any player taking damage from the following attacks: Nightmare rifts," +
			" an un-cured parasite explosion, Corpse flowers or the Nightmare's Surge. Also, no player can take damage off prayer or have their attacks slowed by the Nightmare spores.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
