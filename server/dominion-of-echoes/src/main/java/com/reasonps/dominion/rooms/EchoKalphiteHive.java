package com.reasonps.dominion.rooms;

import com.reasonps.dominion.bosses.kalphite.Form;
import core.api.ReasonUtils;
import io.ruin.cache.NpcID;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Direction;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.map.object.GameObject;

import java.util.Objects;

import static core.api.ReasonUtils.pos;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-16
 */
public class EchoKalphiteHive extends Dominion {

	private final NPC echoKalphiteQueen = new NPC(NpcID.KALPHITE_QUEEN_6500);

	public EchoKalphiteHive(Player player) {
		try {
			setMap(new DynamicMap().build(13972, 1));
			setHost(player);
			init();
		}
		catch (DynamicMap.DynamicMapBuildException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void movePlayerToInstance(Player player) {
		player.getMovement().teleport(pos(map, 49, 22));
		player.currentDynamicMap = map;
		player.currentMapHandler = this;
		player.getHealthHud().open(true, echoKalphiteQueen.getId(), echoKalphiteQueen.getMaxHp());
		players.add(player);
	}

	@Override
	public void init() {
		echoKalphiteQueen.deathEndListener = (e, k, h) -> {
			echoKalphiteQueen.transform(Form.FIRST.getNpcId());
			k.player.getHealthHud().close();
			if (Objects.nonNull(map))
				new NPC(17013).spawn(ReasonUtils.pos(map, 32, 22), Direction.EAST);
		};
		npcs.add(echoKalphiteQueen.spawn(pos(map, 31, 19)));
		new GameObject(-1, pos(map, 52, 22), 10, 0).spawn();
	}
}
