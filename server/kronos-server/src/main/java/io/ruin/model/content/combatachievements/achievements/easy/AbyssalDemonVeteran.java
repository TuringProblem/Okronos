package io.ruin.model.content.combatachievements.achievements.easy;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class AbyssalDemonVeteran extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.abyssalDemonKills.getKills() >= 30) {
			completed = true;
			complete(player);
		}
	}


	@Override
	public String getName() {
		return "Abyssal Demon Veteran";
	}

	@Override
	public String getDesc() {
		return "Kill 30 Abyssal Demons.";
	}

	@Override
	public Tier getTier() {
		return Tier.EASY;
	}

}
