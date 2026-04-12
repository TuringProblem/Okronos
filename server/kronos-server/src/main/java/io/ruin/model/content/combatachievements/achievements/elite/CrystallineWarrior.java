package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class CrystallineWarrior extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Crystalline Warrior";
	}

	@Override
	public String getDesc() {
		return "Kill the Crystalline Hunllef with a full set of perfected armour equipped.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
