package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ArooNoMore extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Arooo No More";
	}

	@Override
	public String getDesc() {
		return "Kill Cerberus without any of the Summoned Souls being spawned.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
