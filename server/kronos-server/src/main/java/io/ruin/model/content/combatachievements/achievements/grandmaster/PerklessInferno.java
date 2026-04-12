package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerklessInferno extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perkless Inferno";
	}

	@Override
	public String getDesc() {
		return "Complete the Inferno without using any perks.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
