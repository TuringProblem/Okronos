package io.ruin.model.activities.gauntlet.tilerotations;

import io.ruin.api.utils.Random;
import io.ruin.model.activities.gauntlet.TileRotation;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.dynamic.DynamicMap;

public class PhaseTwoRotations extends TileRotation {
	@Override
	public Bounds getTilesBounds(DynamicMap map) {
		Position[][] positions = new Position[][]{
			{new Position(map.swRegion.baseX + 36, map.swRegion.baseY + 52, 1), new Position(map.swRegion.baseX + 43, map.swRegion.baseY + 59, 1)},
			{new Position(map.swRegion.baseX + 34, map.swRegion.baseY + 53, 1), new Position(map.swRegion.baseX + 45, map.swRegion.baseY + 58, 1)},
			{new Position(map.swRegion.baseX + 38, map.swRegion.baseY + 50, 1), new Position(map.swRegion.baseX + 42, map.swRegion.baseY + 61, 1)},
		};
		int randomSpawn = Random.get(0, positions.length - 1);
		Position bottomLeft = positions[randomSpawn][0];
		Position topRight = positions[randomSpawn][1];
		Bounds bounds = new Bounds(bottomLeft.getX(), bottomLeft.getY(), topRight.getX(), topRight.getY(), 1);
		return bounds;
	}
}