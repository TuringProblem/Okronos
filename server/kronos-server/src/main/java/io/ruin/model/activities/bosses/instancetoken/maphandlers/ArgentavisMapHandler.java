package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.cache.NpcID;
import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;

public class ArgentavisMapHandler extends MapHandler {

	@Override
	public void movePlayerToInstance(Player player) {
		player.getMovement().teleport(map.convertX(2143), map.convertY(5526), 3);
		super.movePlayerToInstance(player);
	}

	@Override
	public void init() {
		npcs.add(new NPC(NpcID.GIANT_ROC).spawn(map.convertX(2144), map.convertY(5538), 3, 4));
	}

}


