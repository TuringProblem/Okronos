package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Direction;

public class AkkhaMapHandler extends MapHandler {

	@Override
	public void movePlayerToInstance(Player player) {
		player.getMovement().teleport(map.convertX(3688), map.convertY(5408), 1);
		super.movePlayerToInstance(player);
	}

	@Override
	public void init() {
		npcs.add(new NPC(11790).spawn(map.convertX(3680), map.convertY(5408), 1, Direction.EAST, 3));
		npcs.forEach(npc -> npc.face(Direction.EAST));
	}

}
