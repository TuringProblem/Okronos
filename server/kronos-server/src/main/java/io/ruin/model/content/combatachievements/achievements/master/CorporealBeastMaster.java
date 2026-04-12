package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class CorporealBeastMaster extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.corporealBeastKills.getKills() >= 300) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Corporeal Beast Master";
	}

	@Override
	public String getDesc() {
		return "Kill the Corporeal Beast 300 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
