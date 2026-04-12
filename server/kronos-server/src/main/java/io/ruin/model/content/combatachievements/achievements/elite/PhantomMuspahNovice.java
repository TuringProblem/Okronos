package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PhantomMuspahNovice extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.phantomMuspahKills.getKills() >= 25) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Phantom Muspah Novice";
	}

	@Override
	public String getDesc() {
		return "Kill the Phantom Muspah 25 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
