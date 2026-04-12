package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;

public class CerbMapHandler extends MapHandler {
	private static final int CERBERUS_NPC_ID = 5862;

	@Override
	public void movePlayerToInstance(Player player) {
		player.getMovement().teleport(map.convertX(1304), map.convertY(1309), 0);
		super.movePlayerToInstance(player);
	}

	@Override
	public void init() {
		npcs.add(new NPC(CERBERUS_NPC_ID).spawn(map.convertX(1304), map.convertY(1317), 0, 3));
		npcs.forEach(npc -> npc.isMovementBlocked(false, true));
	}

}
