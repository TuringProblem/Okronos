package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class DemonicRebound extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Demonic Rebound";
	}

	@Override
	public String getDesc() {
		return "Use the Vengeance spell to reflect the damage from the Abyssal Sire's explosion back to him.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}


}
