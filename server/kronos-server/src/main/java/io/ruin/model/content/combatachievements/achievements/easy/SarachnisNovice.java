package io.ruin.model.content.combatachievements.achievements.easy;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class SarachnisNovice extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Sarachnis Novice";
	}

	@Override
	public String getDesc() {
		return "Kill Sarachnis 10 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.EASY;
	}

}
