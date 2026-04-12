package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class NewspaperEnthusiast extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Newspaper Enthusiast";
	}

	@Override
	public String getDesc() {
		return "Kill the Sarachnis with a crush weapon.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}
}
