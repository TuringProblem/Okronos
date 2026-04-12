package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.cache.NpcID;
import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;

public class CorpMapHandler extends MapHandler {
	@Override
	public void movePlayerToInstance(Player player) {
		player.getMovement().teleport(map.convertX(2969), map.convertY(4383), 2);
		super.movePlayerToInstance(player);
	}

	@Override
	public void init() {
		npcs.add(new NPC(NpcID.CORPOREAL_BEAST).spawn(map.convertX(2989), map.convertY(4383), 2, 4));
	}

}

