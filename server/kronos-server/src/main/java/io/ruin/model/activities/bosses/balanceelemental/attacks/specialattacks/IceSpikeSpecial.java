package io.ruin.model.activities.bosses.balanceelemental.attacks.specialattacks;

import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.map.Position;

public class IceSpikeSpecial {
	public void start(NPC npc) {
		npc.getPosition().getRegion().players.forEach(p -> {
			Position targetPosition = p.getPosition().copy();
			World.startEvent(e -> {
				e.setCancelCondition(() -> npc.getCombat().isDead() || p.getCombat().isDead() || p.getPosition().getRegion().id != npc.getPosition().getRegionId());
				World.sendGraphics(2941, 0, 0, targetPosition);
				e.delay(2);
				World.sendGraphics(2942, 0, 0, targetPosition);
				e.delay(4);
				if(p.getPosition().distance(targetPosition) < 1) {
					p.hit(new Hit().randDamage(65, 85));
					p.sendMessage("You have been impaled by an ice spike!");
				}
			});
		});
	}
}
