package io.ruin.model.content.combatachievements.achievements.easy;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class SkeletalWyvernVeteran extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.skeletalWyvernKills.getKills() >= 30) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Skeletal Wyvern Veteran";
	}

	@Override
	public String getDesc() {
		return "Kill 30 Skeletal Wyverns.";
	}

	@Override
	public Tier getTier() {
		return Tier.EASY;
	}

}
