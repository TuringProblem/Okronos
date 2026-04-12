package io.ruin.model.activities.bosses.araxxor;

import io.ruin.model.activities.miscpvm.BasicCombat;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.utility.Random;

public final class Araxyte extends BasicCombat {

	@Override public void init() {
		super.init();
		npc.hitListener = new HitListener().postDamage(this::postDamage);
	}

	private void postDamage(final Hit hit) {
		final Entity attacker = hit.attacker;
		if (attacker != null && Random.get(10) == 0) {
			attacker.envenom(6);
		}
	}

}
