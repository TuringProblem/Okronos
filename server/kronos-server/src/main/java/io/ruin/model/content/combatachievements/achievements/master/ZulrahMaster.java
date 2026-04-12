package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ZulrahMaster extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.zulrahKills.getKills() >= 300) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Zulrah Master";
	}

	@Override
	public String getDesc() {
		return "Kill Zulrah 300 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
