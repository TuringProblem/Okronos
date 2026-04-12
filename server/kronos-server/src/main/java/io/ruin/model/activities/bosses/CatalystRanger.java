package io.ruin.model.activities.bosses;

import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Projectile;

public class CatalystRanger extends NPCCombat {
	private static final Projectile RANGED_PROJECTILE = new Projectile(1302, 20, 31, 35, 35, 10, 0, 32);

	@Override
	public void init() {
		npc.hitListener = new HitListener().preDefend(this::preDefend);
		npc.isMovementBlocked(false, true);
	}

	@Override
	public void follow() {

	}

	@Override
	public boolean attack() {
		rangedAttack();
		return true;
	}

	public void rangedAttack() {
		npc.animate(7923);
		int delay = RANGED_PROJECTILE.send(npc, target);
		Hit hit = new Hit(npc, AttackStyle.RANGED)
			.randDamage(info.max_damage)
			.clientDelay(delay);
		hit.postDamage(t -> {
			t.graphics(1303);
		});
		target.hit(hit);
	}


	@Override
	public void process() {

	}


	private void preDefend(Hit hit) {
		if (hit.attackStyle == null)
			return;
		if (hit.attackStyle.isMagic()) {
			hit.block();
			hit.attacker.player.sendMessage("Only melee attacks can deal damage to this catalyst.");
		} else if (hit.attackStyle.isRanged()) {
			hit.attacker.player.sendMessage("Only melee attacks can deal damage to this catalyst.");
			hit.block();
		}
	}
}
