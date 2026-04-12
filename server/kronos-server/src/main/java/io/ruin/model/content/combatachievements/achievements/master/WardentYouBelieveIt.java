package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class WardentYouBelieveIt extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Warden't you believe it";
	}

	@Override
	public String getDesc() {
		return "Defeat the Wardens with all Wardens invocations activated, at expert level and without dying yourself.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
