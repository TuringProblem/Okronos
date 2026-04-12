package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class GraniteFootwork extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Granite Footwork";
	}

	@Override
	public String getDesc() {
		return "Kill the Grotesque Guardians without taking damage from Dawn's rockfall attack.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
