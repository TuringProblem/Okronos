package io.ruin.model.activities.bosses.balanceelemental.attacks.specialattacks;

import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.map.Position;

public class RockFallSpecial {
	public void start(NPC npc) {
		npc.getPosition().getRegion().players.forEach(p -> {
			World.startEvent(e -> {
				e.setCancelCondition(() -> npc.getCombat().isDead() || p.getCombat().isDead() || p.getPosition().getRegion().id != npc.getPosition().getRegionId());
				for(int i = 0; i < 8; i++) {
					Position targetPosition = p.getPosition().copy();
					World.sendGraphics(2529, 0, 0, targetPosition);
					e.delay(6);
					if(p.getPosition().distance(targetPosition) < 1) {
						p.hit(new Hit().randDamage(35, 60));
					}
					e.delay(2);
				}
			});
		});
	}
}
