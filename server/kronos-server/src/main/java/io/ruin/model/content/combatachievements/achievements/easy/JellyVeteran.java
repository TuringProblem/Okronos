package io.ruin.model.content.combatachievements.achievements.easy;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class JellyVeteran extends CombatAchievement {

	@Override
	public void check(Player player) {
		if (player.jellyKills.getKills() >= 50 && !completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Jelly Veteran";
	}

	@Override
	public String getDesc() {
		return "Kill 50 Jellies.";
	}

	@Override
	public Tier getTier() {
		return Tier.EASY;
	}

}
