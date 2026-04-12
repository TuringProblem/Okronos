package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class HardHitter extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Hard Hitter";
	}

	@Override
	public String getDesc() {
		return "Kill the Giant Mole with 4 or fewer instances of damage.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
