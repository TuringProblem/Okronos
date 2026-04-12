package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class AmascutsRemnant extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Amascut's Remnant";
	}

	@Override
	public String getDesc() {
		return "Complete the Tombs of Amascut at raid level 590 without anyone dying.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
