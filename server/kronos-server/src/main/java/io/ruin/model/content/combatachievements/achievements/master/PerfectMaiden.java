package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerfectMaiden extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perfect Maiden";
	}

	@Override
	public String getDesc() {
		return "Kill The Maiden of Sugadinti without anyone in the team taking damage from the following sources:" +
			" Blood Spawn projectiles and Blood Spawn trails. Also, without taking damage off prayer and without letting any of the Nylocas Matomenos heal The Maiden.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
