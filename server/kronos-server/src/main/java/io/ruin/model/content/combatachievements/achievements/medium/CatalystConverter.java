package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class CatalystConverter extends CombatAchievement {
	@Override
	public void check(Player player) {
		System.out.println("brute kills: " + player.catalystBruteKills.getKills());
		System.out.println("mager kills: " + player.catalystMagerKills.getKills());
		System.out.println("ranger kills: " + player.catalystRangerKills.getKills());
		if (!completed && player.catalystBruteKills.getKills() > 0 && player.catalystMagerKills.getKills() > 0 && player.catalystRangerKills.getKills() > 0) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Catalyst Converter";
	}

	@Override
	public String getDesc() {
		return "Kill one of each Catalyst.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}

}
