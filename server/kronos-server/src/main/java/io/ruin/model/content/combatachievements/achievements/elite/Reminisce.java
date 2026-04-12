package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class Reminisce extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.getPosition().getRegion().id != 11602) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Reminisce";
	}

	@Override
	public String getDesc() {
		return "Kill Commander Zilyana in a private instance with melee only.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
