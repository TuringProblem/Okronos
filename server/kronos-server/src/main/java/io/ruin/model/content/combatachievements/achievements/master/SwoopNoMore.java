package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class SwoopNoMore extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Swoop No More";
	}

	@Override
	public String getDesc() {
		return "Kill Kree'arra in a private instance without taking any melee damage from the boss or his bodyguards.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
