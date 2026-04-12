package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class CorruptedWarrior extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Corrupted Warrior";
	}

	@Override
	public String getDesc() {
		return "Kill the Corrupted Hunllef with a full set of perfected corrupted armour equipped.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
