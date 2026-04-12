package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class GuardiansNoMore extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Guardians No More";
	}

	@Override
	public String getDesc() {
		return "Kill Scorpia without killing her guardians.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
