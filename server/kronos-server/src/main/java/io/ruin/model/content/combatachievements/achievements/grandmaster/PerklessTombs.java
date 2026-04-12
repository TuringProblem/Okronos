package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerklessTombs extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perkless Tombs";
	}

	@Override
	public String getDesc() {
		return "Complete an Expert Tombs of Amascut (Solo) without any perks active.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
