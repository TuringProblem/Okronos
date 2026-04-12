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
public class MediumClueScrollSecondaryDonatorMap {

	private static final Logger log = LoggerFactory.getLogger(MediumClueScrollSecondaryDonatorMap.class);
	private static DynamicMap map;
	@Getter
	private static Position teleportPosition;

	public static void initMap() throws DynamicMap.DynamicMapBuildException {

		map = new DynamicMap()
			.buildSw(11312, 1)
			.buildSe(11568, 1)
			.persistent(true);

		teleportPosition = Position.of(
			map.convertX(2870),
			map.convertY(3108),
			0
		);

		// Make the Map Multi-way
		map.makeDynamicMapMultiCombat();
		// Copy the spawns
		map.copyRealMapNpcSpawnsToDynamicMap(List.of(11312, 11568));
	}
}
