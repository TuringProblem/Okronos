package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;

import java.util.ArrayList;
import java.util.List;

public class BabaMapHandler extends MapHandler {

	@Override
	public void movePlayerToInstance(Player player) {
		player.getMovement().teleport(map.convertX(3801), map.convertY(5408), 0);
		super.movePlayerToInstance(player);
	}

	@Override
	public void init() {
		npcs.add(new NPC(11778).spawn(map.convertX(3814), map.convertY(5408), 0, Direction.WEST, 0));
		npcs.forEach(npc ->
			npc.face(Direction.EAST));

		getTileBlockPositions().forEach(pos ->
			Tile.get(pos, true).flagUnmovable());
	}

	List<Position> positions = new ArrayList<>();

	private List<Position> getTileBlockPositions() {
		positions.clear();
		for (int x = 22; x <= 42; x++) {
			positions.add(new Position(map.swRegion.baseX + x, map.swRegion.baseY + 39, 0));
		}
		return positions;
	}

	@Override
	public void destroy() {
		getTileBlockPositions().forEach(pos ->
			Tile.get(pos, true).unflagUnmovable());
		super.destroy();
	}
}
