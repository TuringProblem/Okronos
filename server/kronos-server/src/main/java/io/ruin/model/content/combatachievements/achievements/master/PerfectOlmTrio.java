package io.ruin.model.content.combatachievements.achievements.master;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;

public class PerfectOlmTrio extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed) {
			completed = true;
			complete(player);
		}
	}

	@Override
	public String getName() {
		return "Perfect Olm (Trio)";
	}

	@Override
	public String getDesc() {
		return "Kill the Great Olm in a trio raid without any team member taking damage from any of the following: Teleport portals, Fire Walls, Healing pools, Crystal Bombs, Crystal Burst or Prayer Orbs. You also cannot let his claws regenerate or take damage from the same acid pool back to back.";
	}

	@Override
	public Tier getTier() {
		return Tier.MASTER;
	}
}
