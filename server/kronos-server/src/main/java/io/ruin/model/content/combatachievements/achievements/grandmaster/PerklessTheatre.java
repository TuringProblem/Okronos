package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerklessTheatre extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perkless Theatre";
	}

	@Override
	public String getDesc() {
		return "Complete the Theatre of Blood (Solo) without using any perks.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
