package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class GrotesqueGuardiansNovice extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.grotesqueGuardianKills.getKills() >= 25) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Grotesque Guardians Novice";
	}

	@Override
	public String getDesc() {
		return "Kill the Grotesque Guardians 25 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}

}
