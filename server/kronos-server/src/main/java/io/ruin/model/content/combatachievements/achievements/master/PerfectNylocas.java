package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerfectNylocas extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perfect Nylocas";
	}

	@Override
	public String getDesc() {
		return "Kill the Nylocas Vasilias without anyone in the team attacking any Nylocas with the wrong attack style," +
			" without letting a pillar collapse and without getting hit by any of the Nylocas Vasilias attacks whilst off prayer.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
