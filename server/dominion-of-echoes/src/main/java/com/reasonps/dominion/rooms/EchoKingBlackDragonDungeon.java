package com.reasonps.dominion.rooms;

import core.api.ReasonUtils;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Direction;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.map.object.GameObject;

import static core.api.ReasonUtils.pos;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-25
 */
public class EchoKingBlackDragonDungeon extends Dominion {

	private static final int ECHO_KING_BLACK_DRAGON = 17004;
	private final NPC echoKBD = new NPC(ECHO_KING_BLACK_DRAGON);

	public EchoKingBlackDragonDungeon(Player player) {
		try {
			setMap(new DynamicMap().build(9033, 1));
			setHost(player);
			init();
		}
		catch (DynamicMap.DynamicMapBuildException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void movePlayerToInstance(Player player) {
		player.getMovement().teleport(pos(map, 32, 9));
		player.currentDynamicMap = map;
		player.currentMapHandler = this;
		player.getHealthHud().open(true, ECHO_KING_BLACK_DRAGON, echoKBD.getMaxHp());
		players.add(player);

	}

	@Override
	public void init() {
		npcs.add(echoKBD.spawn(pos(map, 30, 30)));
		echoKBD.deathEndListener = (e, k, h) -> {
			new NPC(17013).spawn(ReasonUtils.pos(map, 32, 32), Direction.SOUTH);
			k.player.getHealthHud().close();
		};
		new GameObject(-1, pos(map, 31, 8), 4, 0).spawn();
	}
}
