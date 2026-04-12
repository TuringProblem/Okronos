package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class HelpfulSpiritWho extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Helpful spirit who?";
	}

	@Override
	public String getDesc() {
		return "Complete the Tombs of Amascut without using any supplies from the Helpful Spirit and without anyone dying.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
