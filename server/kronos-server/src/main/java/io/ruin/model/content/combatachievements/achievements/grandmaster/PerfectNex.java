package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerfectNex extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perfect Nex";
	}

	@Override
	public String getDesc() {
		return "Kill Nex whilst completing the requirements for 'Shadows move', 'A siphon will solve this', and 'Contain this!'";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
