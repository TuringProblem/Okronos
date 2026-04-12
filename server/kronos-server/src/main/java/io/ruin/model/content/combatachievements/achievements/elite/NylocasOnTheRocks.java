package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class NylocasOnTheRocks extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Nylocas, On the Rocks";
	}

	@Override
	public String getDesc() {
		return "In the Theatre of Blood: Entry Mode, freeze any 4 Nylocas with a single Ice Barrage spell.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}
}
