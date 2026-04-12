package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class SuperiorCreatureNovice extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.superiorCreatureKills.getKills() >= 10) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Superior Creature Novice";
	}

	@Override
	public String getDesc() {
		return "Kill a Superior Creature 10 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}
}
