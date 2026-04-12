package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class AvoidingThoseLittleArms extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Avoiding Those Little Arms";
	}

	@Override
	public String getDesc() {
		return "Kill the Giant Mole without taking damage.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}

}
