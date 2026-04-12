package com.reasonps.dominion.rooms;

import com.reasonps.dominion.bosses.sol_heredit.EchoSolHeredit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.map.dynamic.DynamicMap;

import java.util.ArrayList;
import java.util.List;

import static core.api.ReasonUtils.pos;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-25
 */
public class EchoSolHereditColiseum extends Dominion {

	private final NPC echoHeredit = new NPC(17011);
	private final List<Position> positions = new ArrayList<>();

	public EchoSolHereditColiseum(Player player) {
		try {
			setMap(new DynamicMap().build(7216, 1));
			setHost(player);
			init();
		}
		catch (DynamicMap.DynamicMapBuildException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void init() {
		npcs.add(echoHeredit.spawn(pos(map, 35, 35)));
		echoHeredit.deathEndListener = (e, k, h) -> {
			k.player.set("dominion_of_echos_completed", true);
			((EchoSolHeredit) echoHeredit.getCombat()).getPrisms().forEach(NPC::remove);
			if (map != null)
				new NPC(17013).spawn(pos(map, 32, 32), Direction.NORTH);
			k.player.getHealthHud().close();
		};
	}

	@Override
	public void movePlayerToInstance(Player player) {
		getTileBlockPositions().forEach(pos -> Tile.get(pos, true).flagUnmovable());
		player.getMovement().teleport(pos(map, 28, 35));
		player.currentDynamicMap = map;
		player.currentMapHandler = this;
		player.getHealthHud().open(true, echoHeredit.getId(), echoHeredit.getMaxHp());
		players.add(player);
	}

	private List<Position> getTileBlockPositions() {
		positions.clear();
		for (int y = 3098; y < 3115; y++) {
			positions.add(new Position(map.convertX(1816), map.convertY(y), 0));
		}
		for (int x = 1816; x < 1834; x++) {
			positions.add(new Position(map.convertX(x), map.convertY(3115), 0));
		}
		for (int y = 3098; y < 3115; y++) {
			positions.add(new Position(map.convertX(1833), map.convertY(y), 0));
		}
		for (int x = 1816; x < 1834; x++) {
			positions.add(new Position(map.convertX(x), map.convertY(3098), 0));
		}
		positions.add(new Position(map.convertX(1816), map.convertY(3098), 0));
		positions.add(new Position(map.convertX(1833), map.convertY(3098), 0));
		positions.add(new Position(map.convertX(1816), map.convertY(3115), 0));
		positions.add(new Position(map.convertX(1833), map.convertY(3115), 0));
		Bounds bounds = new Bounds(map.convertX(1831), map.convertY(3098), map.convertX(1833), map.convertY(3100), 0);
		bounds.forEachPos(positions::add);
		Bounds bounds1 = new Bounds(map.convertX(1816), map.convertY(3098), map.convertX(1818), map.convertY(3100), 0);
		bounds1.forEachPos(positions::add);
		Bounds bounds2 = new Bounds(map.convertX(1816), map.convertY(3113), map.convertX(1818), map.convertY(3115), 0);
		bounds2.forEachPos(positions::add);
		Bounds bounds3 = new Bounds(map.convertX(1831), map.convertY(3113), map.convertX(1833), map.convertY(3115), 0);
		bounds3.forEachPos(positions::add);

		//TODO:
		return positions;
	}
}
