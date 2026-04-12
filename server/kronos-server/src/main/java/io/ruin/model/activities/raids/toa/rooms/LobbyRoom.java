package io.ruin.model.activities.raids.toa.rooms;

import io.ruin.model.activities.raids.toa.ToARoom;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.map.object.GameObject;

public class LobbyRoom extends ToARoom {
	@Override
	public void buildRoom() throws DynamicMap.DynamicMapBuildException {
		map = new DynamicMap().build(14160, 1);
	}

	@Override
	public void populateRoom() {
		GameObject obj = Tile.getObject(46167, map.convertX(3548), map.convertY(5134), 0);
		obj.setId(46168);
	}

	@Override
	public Position getEnterPosition() {
		return new Position(map.convertX(3551), map.convertY(5162), 0);
	}
}
