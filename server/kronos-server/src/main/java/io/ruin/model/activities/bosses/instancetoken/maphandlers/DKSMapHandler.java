package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.cache.NpcID;
import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;

public class DKSMapHandler extends MapHandler {

	@Override
	public void movePlayerToInstance(Player player) {
		player.getMovement().teleport(map.convertX(2900), map.convertY(4449), 0);
		super.movePlayerToInstance(player);
	}

	@Override
	public void init() {
		npcs.add(new NPC(NpcID.DAGANNOTH_SUPREME).spawn(map.convertX(2906), map.convertY(4448), 0, 4));
		npcs.add(new NPC(NpcID.DAGANNOTH_REX).spawn(map.convertX(2917), map.convertY(4444), 0, 4));
		npcs.add(new NPC(NpcID.DAGANNOTH_PRIME).spawn(map.convertX(2910), map.convertY(4453), 0, 4));
	}

}


