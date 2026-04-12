package io.ruin.model.activities.raids.toa.bosses.zebak;

import io.ruin.model.entity.npc.NPC;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;

public class Rock {
	NPC npc;

	public Rock(Position pos, int npcId) {
		npc = new NPC(npcId).spawn(pos);
	}

	public NPC getNpc() {
		return npc;
	}

}
