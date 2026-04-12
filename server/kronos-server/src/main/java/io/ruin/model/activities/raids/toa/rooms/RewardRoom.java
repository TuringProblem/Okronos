package io.ruin.model.activities.raids.toa.rooms;

import io.ruin.model.activities.raids.toa.ToARoom;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.map.dynamic.DynamicMap;

public class RewardRoom extends ToARoom {
	@Override
	public void buildRoom() throws DynamicMap.DynamicMapBuildException {
		map = new DynamicMap().build(14672, 1);
	}

	@Override
	public void populateRoom() {
		Tile.getObject(46220, map.convertX(3679), map.convertY(5140), 0);
		NPC osmumten = new NPC(11693).spawn(map.convertX(3678), map.convertY(5144), 0);
		osmumten.face(Direction.NORTH);
		npcs.add(osmumten);
	}

	@Override
	public Position getEnterPosition() {
		return new Position(map.convertX(3680), map.convertY(5170), 0);
	}
}
