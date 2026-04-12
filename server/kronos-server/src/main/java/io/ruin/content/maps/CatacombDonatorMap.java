package io.ruin.content.maps;

import io.ruin.model.map.Position;
import io.ruin.model.map.dynamic.DynamicMap;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-13
 */
public class CatacombDonatorMap {

	private static final Logger log = LoggerFactory.getLogger(CatacombDonatorMap.class);
	private static DynamicMap map;
	@Getter
	private static Position teleportPosition;

	public static void initMap() throws DynamicMap.DynamicMapBuildException {

		map = new DynamicMap()
			.buildSw(6556, 1)
			.buildSe(6812, 1)
			.buildNw(6557, 1)
			.buildNe(6813, 1)
			.persistent(true);
		log.info("Loaded catacomb donator map.");

		teleportPosition = Position.of(
			map.convertX(1666),
			map.convertY(10048)
		);

		// Make the Map Multi-way
		map.makeDynamicMapMultiCombat();
		// Copy main world spawns
		map.copyRealMapNpcSpawnsToDynamicMap(List.of(6556, 6812, 6557, 6813));
	}
}
