package io.ruin.model.activities.raids.toa.rooms;

import io.ruin.model.activities.bosses.instancetoken.InstanceMaps;
import io.ruin.model.activities.raids.toa.ToARoom;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.dynamic.DynamicMap;
import lombok.Getter;

public class TumekenWardenRoom extends ToARoom {
	@Getter
	NPC tumekenWarden;

	@Override
	public void buildRoom() throws DynamicMap.DynamicMapBuildException {
		map = new DynamicMap().build(InstanceMaps.TUMEKEN.getRegionId(), InstanceMaps.TUMEKEN.getHeight());
	}

	@Override
	public void populateRoom() {
		tumekenWarden = new NPC(11761).spawn(map.convertX(3934), map.convertY(5152), 1, Direction.NORTH, 0);
		npcs.add(tumekenWarden);

	}

	@Override
	public Position getEnterPosition() {
		return new Position(map.convertX(3936), map.convertY(5161), 1);
	}
}
