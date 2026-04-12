package io.ruin.model.activities.raids.toa.rooms;

import io.ruin.model.activities.bosses.instancetoken.InstanceMaps;
import io.ruin.model.activities.raids.toa.ToARoom;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.map.dynamic.DynamicMap;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class BabaRoom extends ToARoom {
	@Getter
	NPC baba;

	@Override
	public void buildRoom() throws DynamicMap.DynamicMapBuildException {
		map = new DynamicMap().build(InstanceMaps.BABA.getRegionId(), InstanceMaps.BABA.getHeight());
	}

	@Override
	public void populateRoom() {
		baba = new NPC(11778).spawn(map.convertX(3814), map.convertY(5408), 0, Direction.WEST, 0);
		npcs.add(baba);
		baba.deathEndListener = (entity, killer, killHit) -> getTileBlockPositions()
				.forEach(pos -> Tile.get(pos, true).unflagUnmovable());

		getTileBlockPositions().forEach(pos -> Tile.get(pos, true).flagUnmovable());
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
	public Position getEnterPosition() {
		return new Position(map.convertX(3790), map.convertY(5408), 0);
	}
}
