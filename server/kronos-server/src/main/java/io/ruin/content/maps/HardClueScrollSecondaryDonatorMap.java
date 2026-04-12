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
 * @since 2025-07-14
 */
public class HardClueScrollSecondaryDonatorMap {

	private static final Logger log = LoggerFactory.getLogger(HardClueScrollSecondaryDonatorMap.class);
	private static DynamicMap map;
	@Getter
	private static Position teleportPosition;

	public static void initMap() throws DynamicMap.DynamicMapBuildException {

		map = new DynamicMap()
			.build(14495, 1)
			.persistent(true);

		teleportPosition = Position.of(
			map.convertX(3603),
			map.convertY(10230),
			0
		);

		// Make the Map Multi-way
		map.makeDynamicMapMultiCombat();
		// Spawn in some Wyverns
		map.copyRealMapNpcSpawnsToDynamicMap(List.of(14495));
	}
}
