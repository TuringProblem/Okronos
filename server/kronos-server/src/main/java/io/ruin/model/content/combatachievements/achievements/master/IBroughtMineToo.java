package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.combat.Combat;
import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class IBroughtMineToo extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "I Brought Mine Too";
	}

	@Override
	public String getDesc() {
		return "Defeat Sol Heredit using only a Spear, Hasta or Halberd.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
