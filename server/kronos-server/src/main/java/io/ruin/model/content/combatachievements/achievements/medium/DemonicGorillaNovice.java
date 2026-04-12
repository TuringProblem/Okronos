package io.ruin.model.content.combatachievements.achievements.medium;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class DemonicGorillaNovice extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.demonicGorillaKills.getKills() >= 25) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Demonic Gorilla Novice";
	}

	@Override
	public String getDesc() {
		return "Kill a Demonic gorilla 25 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.MEDIUM;
	}

}
