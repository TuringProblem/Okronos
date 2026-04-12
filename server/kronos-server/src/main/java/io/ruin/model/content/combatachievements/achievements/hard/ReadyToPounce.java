package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ReadyToPounce extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Ready to Pounce";
	}

	@Override
	public String getDesc() {
		return "Kill Sarachnis without her using her range attack twice in a row.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
