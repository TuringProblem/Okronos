package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;

public class AlchemicalHydraMapHandler extends MapHandler {

	@Override
	public void movePlayerToInstance(Player player) {
		player.hydraInstanceKills = 0;
		player.getMovement().teleport(map.convertX(1356), map.convertY(10258), 0);
		super.movePlayerToInstance(player);
	}

	@Override
	public void init() {
		npcs.add(new NPC(8615).spawn(map.convertX(1364), map.convertY(10265), 0, 4));
	}

}

