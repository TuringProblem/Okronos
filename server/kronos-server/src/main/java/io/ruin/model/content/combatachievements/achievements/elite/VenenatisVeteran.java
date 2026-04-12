package io.ruin.model.content.combatachievements.achievements.elite;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class VenenatisVeteran extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.venenatisKills.getKills() >= 150) {
			completed = true;
			complete(player);
		}
	}


	@Override
	public String getName() {
		return "Venenatis Veteran";
	}

	@Override
	public String getDesc() {
		return "Kill Venenatis 150 times.";
	}

	@Override
	public Tier getTier() {
		return Tier.ELITE;
	}

}
