package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.cache.Color;
import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;

import java.util.ArrayList;
import java.util.List;

public class SolHereditMapHandler extends MapHandler {

	List<Position> positions = new ArrayList<>();

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

	@Override
	public void movePlayerToInstance(Player player) {
		getTileBlockPositions().forEach(pos -> Tile.get(pos, true).flagUnmovable());
		player.getMovement().teleport(map.convertX(1825), map.convertY(3103), 0);
		super.movePlayerToSoloInstance(player);
	}

	@Override
	public void init() {
		npcs.add(new NPC(12821).spawn(map.convertX(1824), map.convertY(3109), 0, Direction.SOUTH, 0));
		npcs.forEach(npc -> npc.isMovementBlocked(false, true));
	}

	@Override
	public void destroy() {
		getTileBlockPositions().forEach(pos -> Tile.get(pos, true).unflagUnmovable());
		super.destroy();
	}
}
