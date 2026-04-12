package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Direction;

public class TumekensMapHandler extends MapHandler {

	@Override
	public void movePlayerToInstance(Player player) {
		player.getMovement().teleport(map.convertX(3936), map.convertY(5161), 1);
		super.movePlayerToInstance(player);
	}

	@Override
	public void init() {
		npcs.add(new NPC(11761).spawn(map.convertX(3934), map.convertY(5152), 1, Direction.NORTH, 0));
		npcs.forEach(npc -> npc.isMovementBlocked(false, true));
	}

}
