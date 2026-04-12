package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PhantomMuspahMaster extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.phantomMuspahKills.getKills() >= 150) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Phantom Muspah Master";
	}

	@Override
	public String getDesc() {
		return "Kill the Phantom Muspah 150 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
