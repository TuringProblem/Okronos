package io.ruin.model.activities.bosses;

import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Projectile;
import io.ruin.utility.Misc;

public class CatalystMager extends NPCCombat {
	private static final Projectile PROJECTILE = new Projectile(162, 65, 31, 15, 56, 10, 15, 64);

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
		if (!withinDistance(12))
			return false;
		npc.animate(7923);
		int delay = PROJECTILE.send(npc, target);
		target.hit(new Hit(npc, AttackStyle.MAGIC).randDamage(info.max_damage).clientDelay(delay));
		target.graphics(477, 100, delay);
		final int ticks = (delay * 25) / 600;
		npc.addEvent(event -> {
			event.delay(ticks);
			if (target == null)
				return;
			target.localPlayers().forEach(p -> {
				if (!canAttack(p) || Misc.getDistance(target.getPosition(), p.getPosition()) > 1) {
					return;
				}
				p.hit(new Hit(npc, AttackStyle.MAGIC).randDamage(info.max_damage));
				p.graphics(477, 100, 0);
			});
		});
		return true;
	}


	@Override
	public void process() {

	}


	private void preDefend(Hit hit) {
		if (hit.attackStyle == null)
			return;
		if (hit.attackStyle.isMelee()) {
			hit.block();
			hit.attacker.player.sendMessage("Only ranged attacks can deal damage to this catalyst.");
		} else if (hit.attackStyle.isMagic()) {
			hit.attacker.player.sendMessage("Only ranged attacks can deal damage to this catalyst.");
			hit.block();
		}
	}
}
