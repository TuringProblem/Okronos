package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerfectCorruptedHunllef extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perfect Corrupted Hunllef";
	}

	@Override
	public String getDesc() {
		return "Kill the Corrupted Hunllef without taking damage from:" +
			" Tornadoes, Damaging Floor or Stomp Attacks. Also, do not take damage off prayer and do not attack the Corrupted Hunllef with the wrong weapon.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
