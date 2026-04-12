package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerfectFootwork extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perfect Footwork";
	}

	@Override
	public String getDesc() {
		return "Defeat Sol Heredit without taking any damage from his Spear, Shield, Light beam or Triple Attack.";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
