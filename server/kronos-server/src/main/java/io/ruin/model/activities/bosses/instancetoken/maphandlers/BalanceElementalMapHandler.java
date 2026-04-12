package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.cache.NpcID;
import io.ruin.model.activities.bosses.balanceelemental.Phase;
import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.utility.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BalanceElementalMapHandler extends MapHandler {

	@Override
	public void movePlayerToInstance(Player player) {
		player.getMovement().teleport(map.convertX(2518), map.convertY(9210), 1);
		super.movePlayerToInstance(player);
	}

	@Override
	public void init() {
		npcs.add(new NPC(13530).spawn(map.convertX(2535), map.convertY(9212), 1, 4));
		npcs.forEach(n -> {
				Phase.changeForm(n, null);
		});
	}

	@Override
	public void destroy() {
		super.destroy();
	}
}