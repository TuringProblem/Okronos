package com.reasonps.dominion.bosses;

import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-25
 */
@FunctionalInterface
public interface Attack {
	void invoke(Player target, NPCCombat boss);
}
