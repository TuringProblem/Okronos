
package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Direction;

public class ScurriusMapHandler extends MapHandler {

	@Override
	public void movePlayerToInstance(Player player) {
		player.getMovement().teleport(map.convertX(3290), map.convertY(9868), 0);
		super.movePlayerToSoloInstance(player);
	}

	@Override
	public void init() {
		npcs.add(new NPC(7221).spawn(map.convertX(3298), map.convertY(9867), 0, Direction.WEST, 4));
	}

}
