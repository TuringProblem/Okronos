package core.api;

import io.ruin.model.map.Position;
import io.ruin.model.map.dynamic.DynamicMap;

public class ReasonUtils {

	public static Position pos(DynamicMap map, int x, int y) {
		return pos(map, x, y, 0);
	}

	public static Position pos(DynamicMap map, int x, int y, int z) {
		var baseRegion = map.swRegion;
		var regionX = baseRegion.baseX;
		var regionY = baseRegion.baseY;
		var localX = x;
		var localY = y;
		if (localX > 64) {
			localX = x - baseRegion.dynamicRegionBaseX;
		}
		if (localY > 64) {
			localY = y - baseRegion.dynamicRegionBaseY;
		}
		return new Position(regionX + localX, regionY + localY, z);
	}
}
