package io.ruin.model.activities.bosses.sarachnis;

import io.ruin.cache.Color;
import io.ruin.cache.NpcID;
import io.ruin.model.World;
import io.ruin.model.activities.bosses.instancetoken.InstanceManager;
import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.*;
import io.ruin.model.map.dynamic.DynamicMap;

import java.util.ArrayList;
import java.util.List;

public class SarachnisMapHandler extends MapHandler {
	@Override
	public void movePlayerToInstance(Player player) {
		player.getMovement().teleport(map.convertX(1842), map.convertY(9911), 0);
		super.movePlayerToInstance(player);
	}

	@Override
	public void init() {
		NPC vard = new NPC(8713).spawn(map.convertX(1842), map.convertY(9902), 0, Direction.SOUTH, 3);
		npcs.add(vard);
	}


	@Override
	public void destroy() {
		super.destroy();
	}
}
