package io.ruin.model.content.combatachievements.achievements.hard;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class YarrNoMore extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Yarr No More";
	}

	@Override
	public String getDesc() {
		return "Receive kill-credit for K'ril Tsutsaroth without him using his special attack.";
	}

	@Override
	public Tier getTier() {
		return Tier.HARD;
	}

}
