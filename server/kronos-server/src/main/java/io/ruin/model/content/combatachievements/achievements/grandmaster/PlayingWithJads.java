package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PlayingWithJads extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Playing With Jads";
	}

	@Override
	public String getDesc() {
		return "Complete wave 68 of the Inferno within 30 seconds of the first JalTok-Jad dying.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
