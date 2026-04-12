package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class DemonicDefence extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Demonic Defence";
	}

	@Override
	public String getDesc() {
		return "Kill K'ril Tsutsaroth in a private instance without taking any of his melee hits.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
