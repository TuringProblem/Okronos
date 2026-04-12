package io.ruin.model.activities.bosses.balanceelemental.attacks;

import io.ruin.model.World;
import io.ruin.model.activities.bosses.balanceelemental.Phase;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.Random;

public class MeleeFormAttack {
	public void attack(NPC npc, Phase phase) {
		Entity target = npc.getCombat().getTarget();
		if(target instanceof NPC)
			return;
		npc.animate(phase.getDefaultAttackAnim());
		npc.getPosition().getRegion().players.forEach(p -> {
				World.startEvent(e -> {
					e.setCancelCondition(() -> npc.getCombat().isDead() || p.getCombat().isDead() || npc.getCombat().getTarget() == null);
					e.delay(2);
					int maxHit = 60;
					if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE))
						maxHit = 20;
					if(p.getName().equalsIgnoreCase(npc.getCombat().getTarget().getName())
						|| p.getPosition().distance(target.getPosition()) < 2) {
						p.hit(new Hit(npc).randDamage(maxHit).ignorePrayer());
					}
				});
		});
	}
}
