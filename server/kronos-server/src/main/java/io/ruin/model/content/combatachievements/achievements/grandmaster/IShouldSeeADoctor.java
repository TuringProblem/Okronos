package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class IShouldSeeADoctor extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "I Should See A Doctor";
	}

	@Override
	public String getDesc() {
		return "Kill Nex whilst a player is coughing.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
