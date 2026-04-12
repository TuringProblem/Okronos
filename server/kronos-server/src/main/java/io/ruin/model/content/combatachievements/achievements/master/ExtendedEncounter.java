package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ExtendedEncounter extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Extended Encounter";
	}

	@Override
	public String getDesc() {
		return "Kill Vorkath 25 times without leaving his area.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
