package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class ASiphonWillSolveThis extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "A Siphon Will Solve This";
	}

	@Override
	public String getDesc() {
		return "Kill Nex without letting her heal from her Blood Siphon special attack.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
