package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.cache.NpcID;
import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;

public class SaradominMapHandler extends MapHandler {

	@Override
	public void movePlayerToInstance(Player player) {
		player.privateZilyKills = 0;
		player.privateZilyanaInstance = true;
		player.getMovement().teleport(map.swRegion.baseX + 31, map.swRegion.baseY + 17, 0);
		super.movePlayerToInstance(player);
		map.assignListener(player).onExit((p, logout) -> {
			player.privateZilyKills = 0;
			player.privateZilyanaInstance = false;
		});
	}

	@Override
	public void init() {
		npcs.add(new NPC(NpcID.COMMANDER_ZILYANA).spawn(map.swRegion.baseX + 18, map.swRegion.baseY + 17, 0, 4));
	}

	@Override
	public void destroy() {
		super.destroy();
		players.forEach(plr -> {
			plr.privateZilyanaInstance = false;
			plr.privateZilyKills = 0;
		});
	}
}
