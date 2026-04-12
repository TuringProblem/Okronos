package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class YouAreNotPrepared extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "You Are Not Prepared";
	}

	@Override
	public String getDesc() {
		return "Complete a full Tombs of Amascut with the invocations 'Dehyrated' and 'On a Diet' active without anyone dying.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
