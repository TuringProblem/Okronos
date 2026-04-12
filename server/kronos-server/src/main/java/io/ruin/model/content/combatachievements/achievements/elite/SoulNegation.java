package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class SoulNegation extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}


	@Override
	public String getName() {
		return "Soul Negation";
	}

	@Override
	public String getDesc() {
		return "Kill Cerberus after successfully negating 6 or more attacks from Summoned Souls.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}

}
