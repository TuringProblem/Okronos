package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class TheFlincher extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "The Flincher";
	}

	@Override
	public String getDesc() {
		return "\tKill the Chaos Elemental without taking any damage from its attacks.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
