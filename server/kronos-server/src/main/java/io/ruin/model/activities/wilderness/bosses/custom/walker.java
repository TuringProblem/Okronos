package io.ruin.model.activities.wilderness.bosses.custom;

import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.shared.listeners.HitListener;

public class walker extends NPCCombat {

	@Override
	public void init() {
		npc.hitListener = new HitListener().postDefend(this::postDefend);
	}

	private void postDefend(Hit hit) {
//        if (hit.damage > 100 && !hit.isBlocked())
//            hit.damage = 100;
	}

	@Override
	public void updateLastDefend(Entity attacker) {
		super.updateLastDefend(attacker);
		if (attacker.player != null && !attacker.player.getCombat().isSkulled()) {
			attacker.player.getCombat().skullNormal();
		}
	}

	@Override
	public void follow() {
		follow(8);
	}

	@Override
	public boolean attack() {
		return true;
	}

	@Override
	public void process() {

	}
}
