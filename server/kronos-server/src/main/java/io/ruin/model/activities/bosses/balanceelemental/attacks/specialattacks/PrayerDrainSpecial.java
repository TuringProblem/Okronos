package io.ruin.model.activities.bosses.balanceelemental.attacks.specialattacks;

import io.ruin.model.World;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.map.Position;

public class PrayerDrainSpecial {
	public void start(NPC npc) {
		npc.getPosition().getRegion().players.forEach(p -> {
			World.startEvent(e -> {
				e.setCancelCondition(() -> npc.getCombat().isDead() || p.getCombat().isDead() || p.getPosition().getRegion().id != npc.getPosition().getRegionId());
				Position targetPosition = p.getPosition().copy();
				World.sendGraphics(2910, 0, 0, targetPosition);
				e.delay(2);
				World.sendGraphics(2911, 0, 0, targetPosition);
				e.delay(5);
				if(p.getPosition().distance(targetPosition) < 1) {
					p.graphics(1880);
					p.sendMessage("Your prayer points have been drained!");
					p.getPrayer().drain(99);
				}
			});
		});
	}
}
