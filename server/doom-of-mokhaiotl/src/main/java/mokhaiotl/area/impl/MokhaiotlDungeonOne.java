package mokhaiotl.area.impl;

import io.ruin.model.entity.player.Player;
import io.ruin.model.map.dynamic.DynamicMap;
import mokhaiotl.area.MokhaiotlArena;
import mokhaiotl.npc.DoomOfMokhaiotl;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-28
 */
public class MokhaiotlDungeonOne extends MokhaiotlArena {

	public MokhaiotlDungeonOne(Player player) {
		dom = new DoomOfMokhaiotl(this);
		try {
			setMap(new DynamicMap().build(5269, 1));
			map.addNpc(dom);
			setHost(player);
			init();
		}
		catch (DynamicMap.DynamicMapBuildException e) {
			throw new IllegalStateException(e);
		}
	}
}
