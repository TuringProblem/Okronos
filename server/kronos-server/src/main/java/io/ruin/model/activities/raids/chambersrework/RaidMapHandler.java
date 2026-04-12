package io.ruin.model.activities.raids.chambersrework;

import io.ruin.model.World;
import io.ruin.model.activities.raids.chambersrework.CustomXericRaid;
import io.ruin.model.map.Region;
import io.ruin.model.map.dynamic.DynamicMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RaidMapHandler {

	private static final int REGIONS_PER_RAID = 8; // Adjust based on max regions needed per raid
	private static final int MIN_AVAILABLE_REGIONS = 50; // Minimum free regions to allow new raids

	private static final List<Integer> activeRaidRegions = new ArrayList<>();
	private static final List<Integer> availableRegions = new ArrayList<>();

	public static void init() {
		// Initialize available regions pool
		for (int x = 1; x < 127; x++) {
			for (int y = 1; y < 255; y++) {
				Region region = Region.get((x * 256) + y);
				if (region != null && region.baseX >= 6400 && region.empty) {
					availableRegions.add(region.id);
				}
			}
		}
		System.out.println("[RaidMapHandler] Initialized with " + availableRegions.size() + " available regions");

		// Start monitoring system
		startRegionMonitoring();
	}

	public static DynamicMap[] allocateRaidMaps(CustomXericRaid raid) {
		synchronized (activeRaidRegions) {
			// Check if we have enough free regions
			if (availableRegions.size() < REGIONS_PER_RAID) {
				System.out.println("[RaidMapHandler] Not enough free regions for new raid");
				return null;
			}

			try {
				DynamicMap[] maps = new DynamicMap[3]; // floorOne, floorTwo, olmRoom
				List<Integer> allocatedRegions = new ArrayList<>();

				// Allocate regions for each map
				for (int i = 0; i < 3; i++) {
					Integer regionId = availableRegions.remove(0);
					if (regionId == null) {
						releaseRegions(allocatedRegions);
						return null;
					}
					allocatedRegions.add(regionId);
					maps[i] = new DynamicMap().build(regionId, 0);
				}

				// If successful, add to active regions
				activeRaidRegions.addAll(allocatedRegions);

				return maps;

			} catch (Exception e) {
				System.out.println("[RaidMapHandler] Error allocating raid maps: " + e.getMessage());
				e.printStackTrace();
				return null;
			}
		}
	}

	public static void releaseRaidMaps(CustomXericRaid raid) {
		try {
			List<Integer> regionsToRelease = new ArrayList<>();

			// Collect all region IDs
			if (raid.floorOne != null && raid.floorOne.swRegion != null) {
				regionsToRelease.add(raid.floorOne.swRegion.id);
				if (raid.floorOne.nwRegion != null)
					regionsToRelease.add(raid.floorOne.nwRegion.id);
			}

			if (raid.floorTwo != null && raid.floorTwo.swRegion != null) {
				regionsToRelease.add(raid.floorTwo.swRegion.id);
				if (raid.floorTwo.seRegion != null)
					regionsToRelease.add(raid.floorTwo.seRegion.id);
			}

			if (raid.olmRoom != null && raid.olmRoom.swRegion != null) {
				regionsToRelease.add(raid.olmRoom.swRegion.id);
			}

			// Release regions
			releaseRegions(regionsToRelease);

		} catch (Exception e) {
			System.out.println("[RaidMapHandler] Error releasing raid maps: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void releaseRegions(List<Integer> regionIds) {
		synchronized (activeRaidRegions) {
			for (Integer regionId : regionIds) {
				if (regionId == null) continue;

				Region region = Region.get(regionId);
				if (region != null) {
					// Clean up region
					region.destroy();
					region.dynamicIndex = -1;
					region.dynamicData = null;
					region.empty = true;

					// Move from active to available
					activeRaidRegions.remove(regionId);
					if (!availableRegions.contains(regionId))
						availableRegions.add(regionId);
				}
			}
		}
	}

	private static void startRegionMonitoring() {
		World.startEvent(event -> {
			while (true) {
				event.delay(100); // Check every minute

				synchronized (activeRaidRegions) {
					// Report status
					System.out.println("[RaidMapHandler] Status - Active regions: " +
						activeRaidRegions.size() + ", Available regions: " + availableRegions.size());

					// Force cleanup if running low
					if (availableRegions.size() < MIN_AVAILABLE_REGIONS) {
						System.out.println("[RaidMapHandler] Low on regions - forcing cleanup");
						forceCleanup();
					}
				}
			}
		});
	}

	private static void forceCleanup() {
		// Force cleanup all active raid regions that aren't currently in use
		List<Integer> regionsToCheck = new ArrayList<>(activeRaidRegions);

		for (Integer regionId : regionsToCheck) {
			Region region = Region.get(regionId);
			if (region != null && region.players.isEmpty()) {
				releaseRegions(Collections.singletonList(regionId));
			}
		}
	}
}