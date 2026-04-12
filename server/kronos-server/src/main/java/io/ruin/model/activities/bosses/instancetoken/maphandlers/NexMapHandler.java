package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;

public class NexMapHandler extends MapHandler {
	@Override
	public void movePlayerToInstance(Player player) {
		player.getMovement().teleport(map.convertX(2904), map.convertY(5203), 0);
		super.movePlayerToInstance(player);
	}

	@Override
	public void init() {
		npcs.add(new NPC(11289).spawn(map.convertX(2904), map.convertY(5206), 0, 0));
	}

}
