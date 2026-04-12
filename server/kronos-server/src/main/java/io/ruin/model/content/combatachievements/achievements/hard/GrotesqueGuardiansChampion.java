package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class GrotesqueGuardiansChampion extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.grotesqueGuardianKills.getKills() >= 50) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Grotesque Guardians Champion";
	}

	@Override
	public String getDesc() {
		return "Kill the Grotesque Guardians 50 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
