package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ZulrahVeteran extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.zulrahKills.getKills() >= 150) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Zulrah Veteran";
	}

	@Override
	public String getDesc() {
		return "Kill Zulrah 150 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
