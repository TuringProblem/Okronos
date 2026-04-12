package io.ruin.model.activities.bosses.araxxor;

import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;

public class RupturaAraxtyte extends NPCCombat {
	@Override
	public void init() {
		npc.getDef().ignoreOccupiedTiles = true;
	}

	@Override
	public void follow() {
		follow(1);
	}

	@Override
	public boolean attack() {
		npc.face(target);
		if (npc.isRemoved() || npc.getId() == 13672 || npc.getPosition().getRegion().players.isEmpty() || exploding)
			return false;
		if (withinDistance(1)) {
			exploding = true;
			npc.lock();
			World.startEvent(e -> {
				e.delay(2);
				npc.animate(11504);
				e.delay(2);
				npc.localNpcs().forEach(n -> {
					if (n.getPosition().distance(npc.getPosition()) < 4) {
						int damage = 80 - (n.getPosition().distance(npc.getPosition()) * 16);
						n.hit(new Hit().fixedDamage(damage));
					}
				});
				npc.localPlayers().forEach(p -> {
					if (p.getPosition().distance(npc.getPosition()) < 3) {
						int damage = 80 - (p.getPosition().distance(npc.getPosition()) * 16);
						p.hit(new Hit().fixedDamage(damage));
					}
				});
				npc.remove();
			});
		}
		return true;
	}

	boolean exploding = false;

	@Override
	public void process() {
		if (!npc.getPosition().getRegion().players.isEmpty() && npc.getId() == 13673) {
			if (target == null) {
				target = npc.getPosition().getRegion().players.getFirst();
				npc.attackTargetPlayer();
			}
		}
	}
}
