package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;

import java.util.ArrayList;
import java.util.List;

public class LeviathanMapHandler extends MapHandler {

	List<Position> positions = new ArrayList<>();

	private List<Position> getTileBlockPositions() {
		positions.clear();
		positions.add(new Position(map.convertX(2072), map.convertY(6362), 0));
		positions.add(new Position(map.convertX(2071), map.convertY(6363), 0));
		positions.add(new Position(map.convertX(2070), map.convertY(6364), 0));
		positions.add(new Position(map.convertX(2070), map.convertY(6365), 0));
		positions.add(new Position(map.convertX(2070), map.convertY(6366), 0));
		positions.add(new Position(map.convertX(2070), map.convertY(6367), 0));
		positions.add(new Position(map.convertX(2070), map.convertY(6368), 0));
		positions.add(new Position(map.convertX(2070), map.convertY(6369), 0));
		positions.add(new Position(map.convertX(2069), map.convertY(6370), 0));
		positions.add(new Position(map.convertX(2069), map.convertY(6371), 0));
		positions.add(new Position(map.convertX(2069), map.convertY(6372), 0));
		positions.add(new Position(map.convertX(2070), map.convertY(6373), 0));
		positions.add(new Position(map.convertX(2070), map.convertY(6374), 0));
		positions.add(new Position(map.convertX(2070), map.convertY(6375), 0));
		positions.add(new Position(map.convertX(2070), map.convertY(6376), 0));
		positions.add(new Position(map.convertX(2070), map.convertY(6377), 0));
		positions.add(new Position(map.convertX(2071), map.convertY(6378), 0));
		positions.add(new Position(map.convertX(2071), map.convertY(6379), 0));
		positions.add(new Position(map.convertX(2071), map.convertY(6380), 0));
		positions.add(new Position(map.convertX(2071), map.convertY(6381), 0));
		positions.add(new Position(map.convertX(2071), map.convertY(6382), 0));
		positions.add(new Position(map.convertX(2072), map.convertY(6382), 0));
		positions.add(new Position(map.convertX(2073), map.convertY(6382), 0));
		positions.add(new Position(map.convertX(2074), map.convertY(6382), 0));
		positions.add(new Position(map.convertX(2074), map.convertY(6383), 0));
		positions.add(new Position(map.convertX(2075), map.convertY(6383), 0));
		positions.add(new Position(map.convertX(2076), map.convertY(6383), 0));
		positions.add(new Position(map.convertX(2077), map.convertY(6383), 0));
		positions.add(new Position(map.convertX(2078), map.convertY(6382), 0));
		positions.add(new Position(map.convertX(2079), map.convertY(6382), 0));
		positions.add(new Position(map.convertX(2080), map.convertY(6382), 0));
		positions.add(new Position(map.convertX(2081), map.convertY(6382), 0));
		positions.add(new Position(map.convertX(2082), map.convertY(6383), 0));
		positions.add(new Position(map.convertX(2083), map.convertY(6384), 0));
		positions.add(new Position(map.convertX(2084), map.convertY(6384), 0));
		positions.add(new Position(map.convertX(2085), map.convertY(6384), 0));
		positions.add(new Position(map.convertX(2086), map.convertY(6384), 0));
		positions.add(new Position(map.convertX(2087), map.convertY(6383), 0));
		positions.add(new Position(map.convertX(2088), map.convertY(6383), 0));
		positions.add(new Position(map.convertX(2089), map.convertY(6382), 0));
		positions.add(new Position(map.convertX(2090), map.convertY(6381), 0));
		positions.add(new Position(map.convertX(2091), map.convertY(6380), 0));
		positions.add(new Position(map.convertX(2091), map.convertY(6379), 0));
		positions.add(new Position(map.convertX(2091), map.convertY(6378), 0));
		positions.add(new Position(map.convertX(2091), map.convertY(6377), 0));
		positions.add(new Position(map.convertX(2092), map.convertY(6376), 0));
		positions.add(new Position(map.convertX(2092), map.convertY(6375), 0));
		positions.add(new Position(map.convertX(2092), map.convertY(6374), 0));
		positions.add(new Position(map.convertX(2093), map.convertY(6373), 0));
		positions.add(new Position(map.convertX(2093), map.convertY(6372), 0));
		positions.add(new Position(map.convertX(2093), map.convertY(6371), 0));
		positions.add(new Position(map.convertX(2093), map.convertY(6370), 0));
		positions.add(new Position(map.convertX(2093), map.convertY(6369), 0));
		positions.add(new Position(map.convertX(2092), map.convertY(6368), 0));
		positions.add(new Position(map.convertX(2091), map.convertY(6367), 0));
		positions.add(new Position(map.convertX(2091), map.convertY(6366), 0));
		positions.add(new Position(map.convertX(2091), map.convertY(6365), 0));
		positions.add(new Position(map.convertX(2091), map.convertY(6364), 0));
		positions.add(new Position(map.convertX(2090), map.convertY(6363), 0));
		positions.add(new Position(map.convertX(2089), map.convertY(6362), 0));
		positions.add(new Position(map.convertX(2088), map.convertY(6362), 0));
		positions.add(new Position(map.convertX(2087), map.convertY(6362), 0));
		positions.add(new Position(map.convertX(2086), map.convertY(6362), 0));
		positions.add(new Position(map.convertX(2085), map.convertY(6363), 0));
		positions.add(new Position(map.convertX(2084), map.convertY(6363), 0));
		positions.add(new Position(map.convertX(2083), map.convertY(6363), 0));
		positions.add(new Position(map.convertX(2082), map.convertY(6362), 0));
		positions.add(new Position(map.convertX(2081), map.convertY(6362), 0));
		positions.add(new Position(map.convertX(2080), map.convertY(6361), 0));
		positions.add(new Position(map.convertX(2079), map.convertY(6361), 0));
		positions.add(new Position(map.convertX(2078), map.convertY(6361), 0));
		positions.add(new Position(map.convertX(2077), map.convertY(6361), 0));
		positions.add(new Position(map.convertX(2076), map.convertY(6361), 0));
		positions.add(new Position(map.convertX(2075), map.convertY(6361), 0));
		positions.add(new Position(map.convertX(2074), map.convertY(6362), 0));
		positions.add(new Position(map.convertX(2073), map.convertY(6362), 0));
		positions.add(new Position(map.convertX(2072), map.convertY(6362), 0));
		positions.add(new Position(map.convertX(2071), map.convertY(6363), 0));
		positions.add(new Position(map.convertX(2071), map.convertY(6364), 0));
		positions.add(new Position(map.convertX(2071), map.convertY(6365), 0));
		positions.add(new Position(map.convertX(2071), map.convertY(6366), 0));

		positions.add(new Position(map.convertX(2078), map.convertY(6368), 0));

		int swX = map.convertX(2077);
		int swY = map.convertY(6368);
		int neX = map.convertX(2085);
		int neY = map.convertY(6376);
		for (int x = swX; x <= neX; x++) {
			for (int y = swY; y <= neY; y++) {
				positions.add(new Position(x, y, 0));
			}
		}
		return positions;
	}

	@Override
	public void movePlayerToInstance(Player player) {
		player.perfectLeviathanKills = 0;
		player.leviathanTimer = new ActivityTimer();
		getTileBlockPositions().forEach(pos -> Tile.get(pos, true).flagUnmovable());
		player.getMovement().teleport(map.convertX(2072), map.convertY(6369), 0);
		super.movePlayerToInstance(player);
	}

	@Override
	public void init() {
		npcs.add(new NPC(12214).spawn(map.convertX(2078), map.convertY(6369), 0, Direction.WEST, 0));
		npcs.forEach(npc -> {
			npc.animate(10291);
			npc.isMovementBlocked(false, true);
		});
	}

	@Override
	public void destroy() {
		getTileBlockPositions().forEach(pos -> Tile.get(pos, true).unflagUnmovable());
		super.destroy();
	}
}
