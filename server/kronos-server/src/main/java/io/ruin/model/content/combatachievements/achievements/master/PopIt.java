package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PopIt extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Pop It";
	}

	@Override
	public String getDesc() {
		return "Kill Verzik without any Nylocas being frozen and without anyone taking damage from the Nylocas.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
