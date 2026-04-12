package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;

import java.util.ArrayList;
import java.util.List;

public class WhispererMapHandler extends MapHandler {

	@Override
	public void movePlayerToInstance(Player player) {
		player.perfectWhispererKills = 0;
		getTileBlockPositions().forEach(pos -> Tile.get(pos, true).flagUnmovable());
		player.getMovement().teleport(map.convertX(2657), map.convertY(6373), 0);
		super.movePlayerToInstance(player);
	}

	List<Position> positions = new ArrayList<>();

	private List<Position> getTileBlockPositions() {
		positions.clear();
		for (int i = 0; i < 17; i++) {
			positions.add(new Position(map.convertX(2648 + i), map.convertY(6355), 0));
		}
		for (int i = 0; i < 32; i++) {
			positions.add(new Position(map.convertX(2648), map.convertY(6355 + i), 0));
		}
		for (int i = 0; i < 17; i++) {
			positions.add(new Position(map.convertX(2647 + i), map.convertY(6380), 0));
		}
		for (int i = 0; i < 25; i++) {
			positions.add(new Position(map.convertX(2665), map.convertY(6355 + i), 0));
		}

		return positions;
	}

	@Override
	public void init() {
		npcs.add(new NPC(12204).spawn(map.convertX(2657), map.convertY(6363), 0, Direction.NORTH, 0));
	}

	@Override
	public void destroy() {
		getTileBlockPositions().forEach(pos -> Tile.get(pos, true).unflagUnmovable());
		super.destroy();
	}
}