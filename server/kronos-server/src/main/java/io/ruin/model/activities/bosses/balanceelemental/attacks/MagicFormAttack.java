package io.ruin.model.activities.bosses.balanceelemental.attacks;

import io.ruin.model.World;
import io.ruin.model.activities.bosses.balanceelemental.Phase;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.Random;

public class MagicFormAttack {
	private static final Projectile MAGIC_PROJECTILE = new Projectile(2781, 65, 31, 15, 56, 14, 15, 64);
	public void attack(NPC npc, Phase phase) {
		npc.animate(phase.getDefaultAttackAnim());
		npc.getPosition().getRegion().players.forEach(p -> {
			World.startEvent(e -> {
				e.setCancelCondition(() -> npc.getCombat().isDead() || p.getCombat().isDead() || npc.getCombat().getTarget() == null);
				e.delay(2);
				int delay = MAGIC_PROJECTILE.send(npc, p);
				e.delay(World.getTicks(delay) + 1);
				int maxHit = 69;
				if(p.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
					maxHit = 20;
				p.hit(new Hit(npc).randDamage(maxHit).ignorePrayer());
			});
		});
	}

}
