package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class CorporealBeastVeteran extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.corporealBeastKills.getKills() >= 100) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Corporeal Beast Veteran";
	}

	@Override
	public String getDesc() {
		return "Defeat the Corporeal Beast 100 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
