package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class GrotesqueGuardiansSpeedTrialist extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Grotesque Guardians Speed-Trialist";
	}

	@Override
	public String getDesc() {
		return "Kill the Grotesque Guardians in less than 1 minute.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
