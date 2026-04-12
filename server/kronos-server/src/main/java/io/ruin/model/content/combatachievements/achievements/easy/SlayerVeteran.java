package io.ruin.model.content.combatachievements.achievements.easy;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class SlayerVeteran extends CombatAchievement {

	@Override
	public void check(Player player) {
		if (player.slayerTaskKills >= 500 && !completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Slayer Veteran";
	}

	@Override
	public String getDesc() {
		return "Kill 500 npcs on slayer task.";
	}

	@Override
	public Tier getTier() {
		return Tier.EASY;
	}

}
