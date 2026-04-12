package io.ruin.model.activities.raids.toa.rooms;

import io.ruin.model.activities.bosses.instancetoken.InstanceMaps;
import io.ruin.model.activities.raids.toa.ToARoom;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.dynamic.DynamicMap;
import lombok.Getter;

public class AkkhaRoom extends ToARoom {
	@Getter
	NPC akkha;

	@Override
	public void buildRoom() throws DynamicMap.DynamicMapBuildException {
		map = new DynamicMap().build(InstanceMaps.AKKHA.getRegionId(), InstanceMaps.AKKHA.getHeight());
	}

	@Override
	public void populateRoom() {
		akkha = new NPC(11790).spawn(map.convertX(3680), map.convertY(5408), 1, Direction.EAST, 3);
		akkha.getDef().flightClipping = true;
		npcs.add(akkha);
	}

	@Override
	public Position getEnterPosition() {
		return new Position(map.convertX(3698), map.convertY(5408), 1);
	}
}
