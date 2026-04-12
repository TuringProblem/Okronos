package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.cache.NpcID;
import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;

public class KBDMapHandler extends MapHandler {

	@Override
	public void movePlayerToInstance(Player player) {
		player.inKbdInstance = true;
		player.privateKBDKills = 0;
		player.getMovement().teleport(map.convertX(2271), map.convertY(4680), 0);
		super.movePlayerToInstance(player);
	}

	@Override
	public void init() {
		npcs.add(new NPC(NpcID.KING_BLACK_DRAGON).spawn(map.convertX(2274), map.convertY(4704), 0, 4));
	}

	@Override
	public void destroy() {
		super.destroy();
		players.forEach(plr -> {
			plr.inKbdInstance = false;
			plr.privateKBDKills = 0;
		});
	}
}