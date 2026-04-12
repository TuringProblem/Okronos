package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerfectKephri extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perfect Kephri";
	}

	@Override
	public String getDesc() {
		return "Defeat Kephri in a group of two or more with all Kephri invocations active and" +
			" without anyone taking any damage from the following: egg explosions, Kephri's attacks, Exploding Scarabs, Bodyguards, dung attacks. No eggs may hatch throughout the fight.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
