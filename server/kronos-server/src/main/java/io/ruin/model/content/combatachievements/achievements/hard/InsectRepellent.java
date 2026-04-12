package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class InsectRepellent extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Insect Repellent";
	}

	@Override
	public String getDesc() {
		return "Kill Sarachnis without her dealing damage to anyone.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
