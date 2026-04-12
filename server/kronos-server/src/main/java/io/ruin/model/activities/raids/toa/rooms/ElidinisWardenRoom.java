package io.ruin.model.activities.raids.toa.rooms;

import io.ruin.model.activities.bosses.instancetoken.InstanceMaps;
import io.ruin.model.activities.raids.toa.ToARoom;
import io.ruin.model.activities.raids.toa.bosses.warden.ElidinisWarden;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.dynamic.DynamicMap;
import lombok.Getter;

public class ElidinisWardenRoom extends ToARoom {
	@Getter
	NPC elidinisWarden;

	@Override
	public void buildRoom() throws DynamicMap.DynamicMapBuildException {
		map = new DynamicMap().build(InstanceMaps.ELIDINIS.getRegionId(), InstanceMaps.ELIDINIS.getHeight());
	}

	@Override
	public void populateRoom() {
		elidinisWarden = new NPC(11753).spawn(map.convertX(3799), map.convertY(5154), 1, Direction.EAST, 0);
		npcs.add(elidinisWarden);
	}

	@Override
	public Position getEnterPosition() {
		return new Position(map.convertX(3808), map.convertY(5177), 1);
	}
}
