package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class NightmareNovice extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.nightmareKills.getKills() >= 25) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "The Nightmare Novice";
	}

	@Override
	public String getDesc() {
		return "Kill The Nightmare 25 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
