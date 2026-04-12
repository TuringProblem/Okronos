package com.reasonps.dominion.bosses.sol_heredit;

import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.shared.listeners.HitListener;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-11
 */
public class HealingTotemCombat extends NPCCombat {

	@Override
	public void init() {
		npc.hitListener = new HitListener()
			.postDamage(this::startDeath);
	}

	@Override
	public boolean allowRetaliate(Entity attacker) {
		return false;
	}

	@Override
	public boolean isAllowRetaliate() {
		return false;
	}

	@Override
	public void follow() {}

	@Override
	public boolean attack() {
		return false;
	}

	@Override
	public void process() {

	}
}
