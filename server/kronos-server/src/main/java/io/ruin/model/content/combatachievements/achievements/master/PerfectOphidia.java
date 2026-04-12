package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerfectOphidia extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perfect Ophidia";
	}

	@Override
	public String getDesc() {
		return "Kill Ophidia without anyone taking any unavoidable damage from the sources: Auto attacks off prayer, Soldier snakes and Lightning.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
