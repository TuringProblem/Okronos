package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Direction;

public class ElidinisMapHandler extends MapHandler {

	@Override
	public void movePlayerToInstance(Player player) {
		player.getMovement().teleport(map.convertX(3807), map.convertY(5162), 1);
		super.movePlayerToInstance(player);
	}

	@Override
	public void init() {
		npcs.add(new NPC(11753).spawn(map.convertX(3799), map.convertY(5154), 1, Direction.EAST, 0));
		npcs.forEach(npc -> {
			npc.face(Direction.EAST);
			npc.isMovementBlocked(false, true);
		});
	}

}
