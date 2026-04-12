package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Direction;

public class DukeMapHandler extends MapHandler {

	@Override
	public void movePlayerToInstance(Player player) {
		player.perfectDukeKills = 0;
		player.getMovement().teleport(map.convertX(3039), map.convertY(6435), 0);
		super.movePlayerToInstance(player);
	}

	@Override
	public void init() {
		npcs.add(new NPC(12191).spawn(map.convertX(3036), map.convertY(6452), 0, Direction.SOUTH, 0));
		npcs.forEach(npc -> npc.isMovementBlocked(false, true));
	}
}