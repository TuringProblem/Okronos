package io.ruin.model.activities.bosses.nightmare;

import io.ruin.model.World;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;

public class Spore extends GameObject {

	int state = -1;
	NPC nm;

	public Spore(Position location, NPC nm) {

		super(37739, location.getX(), location.getY(), 3, 10, 0);
		spawn();
		this.nm = nm;
		check();
	}

	private void check() {
		if (state == 2) {
			remove();
			return;
		} else if (state > -1) {
			state++;
		}
		World.startEvent(e -> {
			e.setCancelCondition(this::isRemoved);
			e.delay(1);
			if (state == -1) {
				for (Player p : World.players()) {
					if (p.getPosition().distance(new Position(x, y, z)) < 2) {
						p.set("KROTA_spore", System.currentTimeMillis() + 30000);
						VarPlayerRepository.RUNNING.set(p, 0);
						NightmareCombat combat = (NightmareCombat) nm.getCombat();
						combat.damagedPlayer = true;
//						p.hit(new Hit().randDamage(35));
						animate(getDef().animationID + 1);
						state = 0;
					}
				}
			}
			check();
		});
	}


}
