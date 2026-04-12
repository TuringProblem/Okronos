package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;

public class AraxxorMapHandler extends MapHandler {
	@Override
	public void movePlayerToInstance(Player player) {
		player.getMovement().teleport(map.convertX(3647), map.convertY(9815), 0);
		super.movePlayerToInstance(player);
	}

	@Override
	public void init() {
		npcs.add(new NPC(13668).spawn(map.convertX(3633), map.convertY(9816), 0, 4));
	}

}


