package io.ruin.model.content.combatachievements.achievements.grandmaster;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerfectTheatre extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perfect Theatre";
	}

	@Override
	public String getDesc() {
		return "Complete the Theatre of Blood without anyone dying through any means and whilst everyone in the team completes the following Combat Achievement tasks in a single run:" +
			" \"Perfect Maiden\", \"Perfect Bloat\", \"Perfect Nylocas\", \"Perfect Sotetseg\", \"Perfect Xarpus\" and \"Perfect Verzik\".";
	}

	@Override
	public Tier getTier() {
		return Tier.GRANDMASTER;
	}
}
