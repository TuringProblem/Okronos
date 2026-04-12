package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ZulrahSpeedTrialist extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Zulrah Speed-Trialist";
	}

	@Override
	public String getDesc() {
		return "Kill Zulrah in less than 30 seconds, without a slayer task.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
