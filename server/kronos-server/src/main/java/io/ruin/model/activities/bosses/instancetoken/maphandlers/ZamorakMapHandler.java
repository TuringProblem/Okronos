package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.cache.NpcID;
import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;

public class ZamorakMapHandler extends MapHandler {

	@Override
	public void movePlayerToInstance(Player player) {
		player.privateKrilInstance = true;
		player.privateKrilKills = 0;
		player.getMovement().teleport(map.swRegion.baseX + 13, map.swRegion.baseY + 24, 2);
		super.movePlayerToInstance(player);
		map.assignListener(player).onExit((p, logout) -> {
			player.privateKrilKills = 0;
			player.privateKrilInstance = false;
		});
	}

	@Override
	public void init() {
		npcs.add(new NPC(NpcID.KRIL_TSUTSAROTH).spawn(map.swRegion.baseX + 11, map.swRegion.baseY + 12, 2, 4));
	}

	@Override
	public void destroy() {
		super.destroy();
		players.forEach(plr -> {
			plr.privateKrilKills = 0;
			plr.privateKrilInstance = false;
		});
	}
}
