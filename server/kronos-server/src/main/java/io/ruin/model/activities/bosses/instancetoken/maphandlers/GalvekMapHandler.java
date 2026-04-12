package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.cache.NpcID;
import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;

public class GalvekMapHandler extends MapHandler {

	@Override
	public void movePlayerToInstance(Player player) {
		player.getMovement().teleport(map.convertX(1630), map.convertY(5720), 2);
		players.add(player);
		super.movePlayerToInstance(player);
	}

	@Override
	public void init() {
		npcs.add(new NPC(NpcID.GALVEK_8096).spawn(map.convertX(1639), map.convertY(5723), 2, 0));
	}

}

