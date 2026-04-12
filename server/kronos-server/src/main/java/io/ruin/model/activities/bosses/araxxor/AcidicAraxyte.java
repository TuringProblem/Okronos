package io.ruin.model.activities.bosses.araxxor;

import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.map.Projectile;

public class AcidicAraxyte extends NPCCombat {
	private static final Projectile RANGED_PROJECTILE = new Projectile(1560, 60, 31, 35, 35, 10, 0, 32);
	private static final int RANGED_HIT_GFX = 1303;

	@Override
	public void init() {

	}

	@Override
	public void follow() {

	}

	@Override
	public boolean attack() {
		rangedAttack();
		return true;
	}

	private void rangedAttack() {
		npc.animate(11498);
		npc.face(target);
		int delay = RANGED_PROJECTILE.send(npc, target);
		Hit hit = new Hit(npc, AttackStyle.RANGED)
			.randDamage(info.max_damage)
			.clientDelay(delay);
		hit.postDamage(t -> {
			t.graphics(RANGED_HIT_GFX);
		});
		target.hit(hit);
	}


	@Override
	public void process() {

	}
}
