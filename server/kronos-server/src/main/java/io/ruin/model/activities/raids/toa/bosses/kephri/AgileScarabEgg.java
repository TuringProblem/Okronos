package io.ruin.model.activities.raids.toa.bosses.kephri;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.entity.npc.NPC;


public class AgileScarabEgg extends NPC {
	public AgileScarabEgg(int id, NPC boss) {
		super(id);
		World.startEvent(e -> {
			e.delay(18);
			if (getHp() > 0) {
				Kephri n = (Kephri) boss.getCombat();
				n.perfectKephriFailed = true;
				npc.transform(11727);
				if (!npc.getPosition().getRegion().players.isEmpty())
					npc.getCombat().setTarget(Random.get(npc.getPosition().getRegion().players));
				else if (!npc.localPlayers().isEmpty())
					npc.getCombat().setTarget(Random.get(npc.localPlayers()));

			}
		});
	}
}
