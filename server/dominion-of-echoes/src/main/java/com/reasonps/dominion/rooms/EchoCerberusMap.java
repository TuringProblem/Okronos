package com.reasonps.dominion.rooms;

import core.api.ReasonUtils;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Direction;
import io.ruin.model.map.dynamic.DynamicMap;

import static core.api.ReasonUtils.pos;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-25
 */
public class EchoCerberusMap extends Dominion {

	private final NPC echoCerberus = new NPC(17000);

	public EchoCerberusMap(Player player) {
		try {
			setMap(new DynamicMap().build(5140, 1));
			setHost(player);
			init();
		}
		catch (DynamicMap.DynamicMapBuildException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void movePlayerToInstance(Player player) {
		player.getMovement().teleport(pos(map, 24, 10));
		player.currentDynamicMap = map;
		player.currentMapHandler = this;
		player.getHealthHud().open(true, echoCerberus.getId(), echoCerberus.getMaxHp());
		players.add(player);
	}

	@Override
	public void init() {
		echoCerberus.deathEndListener = (e, k, h) -> {
			new NPC(17014)
				.spawn(ReasonUtils.pos(map, 24, 37), Direction.SOUTH);
			k.player.getHealthHud().close();
		};
		npcs.add(echoCerberus.spawn(pos(map, 22, 34)));
	}
}
