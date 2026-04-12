package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class AppropriateTools extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Appropriate Tools";
	}

	@Override
	public String getDesc() {
		return "Defeat the Pestilent Bloat in the Theatre of Blood with everyone having a salve amulet equipped.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
