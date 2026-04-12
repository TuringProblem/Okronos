package com.reasonps.dominion.rooms;

import com.reasonps.dominion.bosses.hunllef.EchoHunllef;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Direction;
import io.ruin.model.map.dynamic.DynamicChunk;
import io.ruin.model.map.dynamic.DynamicMap;

import java.util.ArrayList;

import static core.combat.api.ReasonUtils.pos;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-27
 */
public class EchoHunllefMap extends Dominion {

	private final NPC echoHunllef = new NPC(17006);

	public EchoHunllefMap(Player player) {
		try {
			var bossChunks = new ArrayList<DynamicChunk>();
			for (int i = 0; i < 2; i++)
				for (int j = 0; j < 2; j++)
					bossChunks.add(new DynamicChunk(246 + i, 710 + j, 1)
						.pos(4 + i, 6 + j, 1));

			setMap(new DynamicMap().build(bossChunks));
			setHost(player);
			init();
		}
		catch (DynamicMap.DynamicMapBuildException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void movePlayerToInstance(Player player) {
		player.getMovement().teleport(pos(map, 40, 60, 1));
		player.currentDynamicMap = map;
		player.currentMapHandler = this;
		player.getHealthHud().open(true, echoHunllef.getId(), echoHunllef.getMaxHp());
		players.add(player);
	}

	@Override
	public void init() {
		npcs.add(echoHunllef.spawn(pos(map, 38, 51, 1)));
		echoHunllef.deathEndListener = (e, killer, h) ->
			((EchoHunllef) echoHunllef.getCombat()).startDeath(killer, map);
		echoHunllef.face(Direction.NORTH);
		echoHunllef.forceMovementUpdate.send(echoHunllef);
	}
}
