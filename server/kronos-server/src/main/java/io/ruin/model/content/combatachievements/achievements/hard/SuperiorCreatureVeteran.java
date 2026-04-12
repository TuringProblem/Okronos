package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class SuperiorCreatureVeteran extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.superiorCreatureKills.getKills() >= 75) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Superior Creature Veteran";
	}

	@Override
	public String getDesc() {
		return "Kill a superior creature 75 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
