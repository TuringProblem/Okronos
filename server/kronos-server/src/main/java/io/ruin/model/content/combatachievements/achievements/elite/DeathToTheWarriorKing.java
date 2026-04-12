package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.combat.Combat;
import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class DeathToTheWarriorKing extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Death to the Warrior King";
	}

	@Override
	public String getDesc() {
		return "Kill Dagannoth Rex whilst under attack by Dagannoth Supreme and Dagannoth Prime.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
