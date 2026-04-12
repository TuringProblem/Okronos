package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class VenenatisAdept extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Venenatis Adept";
	}

	@Override
	public String getDesc() {
		return "Kill Venenatis once.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}

}
