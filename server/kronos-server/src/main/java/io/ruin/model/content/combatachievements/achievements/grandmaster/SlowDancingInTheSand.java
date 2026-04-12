package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class SlowDancingInTheSand extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Slow Dancing in the Sand";
	}

	@Override
	public String getDesc() {
		return "Defeat Sol Heredit without running during the fight with him.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
