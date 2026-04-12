package io.ruin.model.activities.gauntlet;

import io.ruin.model.map.Bounds;
import io.ruin.model.map.dynamic.DynamicMap;

public abstract class TileRotation {
	public abstract Bounds getTilesBounds(DynamicMap map);
}
