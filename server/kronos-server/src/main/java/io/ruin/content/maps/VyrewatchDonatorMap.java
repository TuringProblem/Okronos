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
public class VyrewatchDonatorMap {

	private static final Logger log = LoggerFactory.getLogger(VyrewatchDonatorMap.class);
	private static DynamicMap map;
	@Getter
	private static Position teleportPosition;

	public static void initMap() throws DynamicMap.DynamicMapBuildException {

		map = new DynamicMap()
			.buildSw(14388, 1)
			.buildSe(14644, 1)
			.persistent(true);
		log.info("Loaded vyrewatch donator map.");

		teleportPosition = Position.of(
			map.convertX(3605),
			map.convertY(3366),
			0
		);

		// Make the Map Multi-way
		map.makeDynamicMapMultiCombat();
		// Copy main world spawns
		map.copyRealMapNpcSpawnsToDynamicMap(List.of(14388, 14644));
	}
}
