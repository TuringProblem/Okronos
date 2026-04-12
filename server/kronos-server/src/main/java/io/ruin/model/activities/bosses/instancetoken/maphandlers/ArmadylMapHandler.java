package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.cache.NpcID;
import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;

public class ArmadylMapHandler extends MapHandler {

	@Override
	public void movePlayerToInstance(Player player) {
		player.privateKreeInstance = true;
		player.privateKreeKills = 0;
		player.getMovement().teleport(map.swRegion.baseX + 23, map.swRegion.baseY + 12, 2);
		super.movePlayerToInstance(player);
		map.assignListener(player).onExit((p, logout) -> {
			player.privateKreeInstance = false;
			player.privateKreeKills = 0;
		});
	}

	@Override
	public void init() {
		npcs.add(new NPC(NpcID.KREEARRA).spawn(map.swRegion.baseX + 17, map.swRegion.baseY + 22, 2, 4));
	}

	@Override
	public void destroy() {
		super.destroy();
		players.forEach(plr -> {
			plr.privateKreeInstance = false;
			plr.privateKreeKills = 0;
		});
	}
}