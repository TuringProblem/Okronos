package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;

public class PhantomMuspahMapHandler extends MapHandler {
	@Override
	public void movePlayerToInstance(Player player) {
		player.privatePhantomMuspahKills = 0;
		player.getMovement().teleport(map.convertX(2859), map.convertY(4259), 0);
		super.movePlayerToInstance(player);
	}

	@Override
	public void init() {
		npcs.add(new NPC(12077).spawn(map.convertX(2847), map.convertY(4258), 0, 4));
		npcs.forEach(npc -> npc.isMovementBlocked(false, true));
	}

}