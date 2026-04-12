package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class SkotizoNovice extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.skotizoKills.getKills() >= 5) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Skotizo Novice";
	}

	@Override
	public String getDesc() {
		return "Kill Skotizo 5 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}

}
