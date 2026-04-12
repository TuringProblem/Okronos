package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class HotOnYourFeet extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Hot On Your Feet";
	}

	@Override
	public String getDesc() {
		return "Kill the Corporeal Beast without anyone killing the dark core or taking damage from the dark core.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
