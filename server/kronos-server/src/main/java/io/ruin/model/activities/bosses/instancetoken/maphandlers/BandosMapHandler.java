package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.cache.NpcID;
import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;

public class BandosMapHandler extends MapHandler {

	@Override
	public void movePlayerToInstance(Player player) {
		player.privateBandosInstance = true;
		player.privateBandosKills = 0;
		player.getMovement().teleport(map.swRegion.baseX + 3, map.swRegion.baseY + 10, 2);
		super.movePlayerToInstance(player);
		map.assignListener(player).onExit((p, logout) -> {
			player.privateBandosInstance = false;
			player.privateBandosKills = 0;
		});
	}

	@Override
	public void init() {
		npcs.add(new NPC(NpcID.GENERAL_GRAARDOR).spawn(map.swRegion.baseX + 11, map.swRegion.baseY + 12, 2, 4));
	}

	@Override
	public void destroy() {
		super.destroy();
		players.forEach(plr -> {
			plr.privateBandosKills = 0;
			plr.privateBandosInstance = false;
		});
	}
}
