package io.ruin.model.content.combatachievements.achievements.easy;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class NechryaelVeteran extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.nechryaelKills.getKills() >= 30) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Nechyrael Veteran";
	}

	@Override
	public String getDesc() {
		return "Kill 30 Nechyrael.";
	}

	@Override
	public Tier getTier() {
		return Tier.EASY;
	}

}
