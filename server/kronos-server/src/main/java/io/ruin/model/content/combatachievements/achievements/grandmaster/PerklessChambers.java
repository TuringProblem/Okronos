package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerklessChambers extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perkless Chambers";
	}

	@Override
	public String getDesc() {
		return "Complete the Chambers of Xeric (Solo) without any perks active.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
