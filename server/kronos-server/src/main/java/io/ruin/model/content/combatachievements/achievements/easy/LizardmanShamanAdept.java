package io.ruin.model.content.combatachievements.achievements.easy;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class LizardmanShamanAdept extends CombatAchievement {

	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}


	@Override
	public String getName() {
		return "Lizardman Shaman Adept";
	}

	@Override
	public String getDesc() {
		return "Kill a lizardman shaman.";
	}

	@Override
	public Tier getTier() {
		return Tier.EASY;
	}

}
