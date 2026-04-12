
package io.ruin.model.map.dynamic;

import io.ruin.cache.LocType;
import io.ruin.model.World;
import io.ruin.model.activities.wilderness.Wilderness;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.MapListener;
import io.ruin.model.map.Position;
import io.ruin.model.map.Region;
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.GameObject;
import io.ruin.network.incoming.handlers.WalkHandler;
import io.ruin.process.event.Event;
import io.ruin.process.event.EventConsumer;
import io.ruin.process.event.EventWorker;
import io.ruin.util.CheckedConcurrentLinkedDeque;
import io.ruin.util.TimeHolder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static core.task.api.API.fork;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class DynamicMap {

	@AllArgsConstructor
	public static class DynamicMapBuildException extends Exception {

		public DynamicMapBuildException(String message) {
			super(message);
		}

		public DynamicMapBuildException(Exception reason) {
			super(reason);
		}
	}

	private static final CheckedConcurrentLinkedDeque<Region> FREE_REGIONS = new CheckedConcurrentLinkedDeque<>(
			region -> region != null && region.baseX >= 6400 && region.empty && region.players.isEmpty());

	public static int STARTING_FREE_REGIONS;

	public static int FREE_REGIONS_SIZE() {
		return FREE_REGIONS.size();
	}

	public Region swRegion, nwRegion, seRegion, neRegion;

	/**
	 * unlike temporary instances like minigames or zulrah, persistent maps are things like donor-zones which exist for
	 * the full lifetime of the server, never closing
	 */
	public boolean persistentMap;
	public String creationStackTrace;

	public DynamicMap() throws DynamicMapBuildException {
		this.creationStackTrace = ExceptionUtils.getStackTrace(new RuntimeException());
		swRegion = FREE_REGIONS.poll();
		if (swRegion == null) {
			throw new DynamicMapBuildException("No regions available to allocate to dynamicmap!");
		}
		// Validate the region is actually usable
		if (swRegion != null && (!swRegion.empty || !swRegion.players.isEmpty())) {
			log.error("[DynamicMap] Warning: Allocated region " + swRegion.id + " may not be fully free");
			swRegion = null;
			return;
		}
		DynamicMapGuard.register(this);
	}

	private boolean validateRegion(Region region) {
		if (region == null)
			return false;
		if (!region.empty)
			return false;
		if (!region.players.isEmpty())
			return false;
		if (region.dynamicData != null)
			return false;
		return true;
	}

	public DynamicMap build(List<DynamicChunk> chunks) throws DynamicMapBuildException {
		// Validate regions before building
		if (swRegion == null || !validateRegion(swRegion)) {
			this.buildFailed("Invalid SW region");
		}

		List<DynamicChunk> swChunks = chunks.stream()
				.filter(c -> c.pointX <= 7 && c.pointY <= 7)
				.collect(Collectors.toList());

		if (!swChunks.isEmpty()) {
			buildRegion(swRegion, swChunks);
		}

		// SE region
		List<DynamicChunk> seChunks = chunks.stream()
				.filter(c -> c.pointX > 7 && c.pointY <= 7)
				.collect(Collectors.toList());
		seChunks.forEach(c -> c.pointX -= 8);
		if (!seChunks.isEmpty()) {
			buildSe(seChunks);
		}

		// NW region
		List<DynamicChunk> nwChunks = chunks.stream()
				.filter(c -> c.pointX <= 7 && c.pointY > 7)
				.collect(Collectors.toList());
		nwChunks.forEach(c -> c.pointY -= 8);
		if (!nwChunks.isEmpty()) {
			buildNw(nwChunks);
		}

		// NE region
		List<DynamicChunk> neChunks = chunks.stream()
				.filter(c -> c.pointX > 7 && c.pointY > 7)
				.collect(Collectors.toList());
		neChunks.forEach(c -> {
			c.pointX -= 8;
			c.pointY -= 8;
		});

		if (!neChunks.isEmpty()) {
			buildNe(neChunks);
		}

		cleanupRegionNPCs();

		return this;
	}

	private void cleanupRegionNPCs() {
		for (Region region : getRegions()) {
			if (region != null) {
				List<NPC> npcsInRegion = new ObjectArrayList<>();
				for (var o : World.npcsSlots()) {
					if (o == null) {
						continue;
					}

					final NPC npc = (NPC) o;
					if (npc.getPosition().getRegion().id == region.id)
						npcsInRegion.add(npc);
				}
				for (NPC npc : npcsInRegion) {
					npc.remove();
				}
			}
		}
	}

	public DynamicMap buildSe(List<DynamicChunk> chunks) throws DynamicMapBuildException {
		seRegion = Region.LOADED[swRegion.id + 256];
		buildRegion(seRegion, chunks);
		cleanupRegionNPCs();
		return this;
	}

	public DynamicMap buildNw(List<DynamicChunk> chunks) throws DynamicMapBuildException {
		nwRegion = Region.LOADED[swRegion.id + 1];
		buildRegion(nwRegion, chunks);
		cleanupRegionNPCs();
		return this;
	}

	public DynamicMap buildNe(List<DynamicChunk> chunks) throws DynamicMapBuildException {
		neRegion = Region.LOADED[swRegion.id + 257];
		buildRegion(neRegion, chunks);
		cleanupRegionNPCs();
		return this;
	}

	public DynamicMap buildSw(List<DynamicChunk> chunks) throws DynamicMapBuildException {
		buildRegion(swRegion, chunks);
		cleanupRegionNPCs();
		return this;
	}

	/**
	 * Build by region
	 */

	public DynamicMap build(int regionId, int maxHeight) throws DynamicMapBuildException {
		return buildSw(regionId, maxHeight);
	}

	public DynamicMap buildSw(int regionId, int maxHeight) throws DynamicMapBuildException {
		build(swRegion, regionId, maxHeight);
		for (Region region : getRegions()) {
			if (region != null) {
				List<NPC> npcsInRegion = new ObjectArrayList<>();
				for (var o : World.npcsSlots()) {
					if (o == null) {
						continue;
					}

					final NPC npc = (NPC) o;
					if (npc.getPosition().getRegion().id == region.id)
						npcsInRegion.add(npc);
				}
				for (NPC npc : npcsInRegion) {
					npc.remove();
				}
			}
		}
		return this;
	}

	public DynamicMap buildNw(int regionId, int maxHeight) throws DynamicMapBuildException {
		build(nwRegion = Region.LOADED[swRegion.id + 1], regionId, maxHeight);
		return this;
	}

	public DynamicMap buildSe(int regionId, int maxHeight) throws DynamicMapBuildException {
		build(seRegion = Region.LOADED[swRegion.id + 256], regionId, maxHeight);
		return this;
	}

	public DynamicMap buildNe(int regionId, int maxHeight) throws DynamicMapBuildException {
		build(neRegion = Region.LOADED[swRegion.id + 257], regionId, maxHeight);
		return this;
	}

	private void build(Region targetRegion, int regionId, int maxHeight) throws DynamicMapBuildException {
		if (targetRegion == null) {
			this.buildFailed("targetRegion is null, unable to copy " + regionId + " z" + maxHeight);
		}

		Region sourceRegion = Region.get(regionId);
		List<DynamicChunk> chunks = new ObjectArrayList<>(64 * (maxHeight + 1));

		for (int pointZ = 0; pointZ <= maxHeight; pointZ++) {
			for (int pointX = 0; pointX < 8; pointX++) {
				int chunkX = (sourceRegion.baseX + (pointX * 8)) >> 3;
				for (int pointY = 0; pointY < 8; pointY++) {
					int chunkY = (sourceRegion.baseY + (pointY * 8)) >> 3;
					chunks.add(new DynamicChunk(chunkX, chunkY, pointZ).pos(pointX, pointY, pointZ));
				}
			}
		}

		targetRegion.dynamicRegionBaseX = sourceRegion.baseX;
		targetRegion.dynamicRegionBaseY = sourceRegion.baseY;

		buildRegion(targetRegion, chunks);

		cleanupRegionNPCs();
	}

	private void buildRegion(Region targetRegion, List<DynamicChunk> chunks) throws DynamicMapBuildException {
		try {
			// Initialize dynamic data
			targetRegion.dynamicData = new int[4][8][8][2];
			List<GameObject> objects = new ObjectArrayList<>();

			// Build chunks
			for (DynamicChunk chunk : chunks) {
				int chunkAbsX = chunk.x << 3;
				int chunkAbsY = chunk.y << 3;

				for (int localX = 0; localX < 8; localX++) {
					for (int localY = 0; localY < 8; localY++) {
						Tile localTargetTile = Tile.get(chunkAbsX + localX, chunkAbsY + localY, chunk.z, false);
						if (localTargetTile == null)
							continue;

						int newChunkAbsX = targetRegion.baseX + (chunk.pointX * 8);
						int newChunkAbsY = targetRegion.baseY + (chunk.pointY * 8);
						int newTileX = newChunkAbsX + DynamicChunk.rotatedX(localX, localY, chunk.rotation);
						int newTileY = newChunkAbsY + DynamicChunk.rotatedY(localX, localY, chunk.rotation);

						Tile newTile = Tile.get(newTileX, newTileY, chunk.pointZ, true);
						if (newTile == null)
							continue;

						// Copy tile properties
						newTile.clipping = localTargetTile.defaultClipping;
						newTile.multi = localTargetTile.multi;
						newTile.roofExists = localTargetTile.roofExists;
						newTile.nearBank = localTargetTile.nearBank;

						// Handle objects
						if (localTargetTile.gameObjects != null) {
							handleTileObjects(localTargetTile, newChunkAbsX, newChunkAbsY, chunk, localX, localY, objects);
						}
					}
				}

				// Set dynamic data
				targetRegion.dynamicData[chunk.pointZ][chunk.pointX][chunk.pointY][0] = (chunk.rotation << 1) | (chunk.z << 24)
						| (chunk.x << 14) | (chunk.y << 3);
				targetRegion.dynamicData[chunk.pointZ][chunk.pointX][chunk.pointY][1] = chunk.regionId;
			}

			// Add objects last for proper clipping
			for (GameObject obj : objects) {
				if (objectConsumer != null)
					objectConsumer.accept(obj);
				if (obj.getId() != -1)
					Tile.get(obj.x, obj.y, obj.z, true).addObject(obj);
			}

			targetRegion.empty = false;
		} catch (Exception e) {
			this.buildFailed(e);
		}
	}

	private void handleTileObjects(Tile sourceTile, int newChunkAbsX, int newChunkAbsY,
			DynamicChunk chunk, int localX, int localY, List<GameObject> objects) {

		for (GameObject obj : sourceTile.gameObjects) {
			if (obj.isSpawned())
				continue;

			LocType def = LocType.get(obj.originalId);
			if (def == null) {
				log.error("[DynamicMap] Missing loc type for object " + obj.originalId);
				continue;
			}

			int newX = newChunkAbsX + DynamicChunk.rotatedX(localX, localY, chunk.rotation,
					def.xLength, def.yLength, obj.getDirection());
			int newY = newChunkAbsY + DynamicChunk.rotatedY(localX, localY, chunk.rotation,
					def.xLength, def.yLength, obj.getDirection());
			int newDirection = (obj.getDirection() + chunk.rotation) & 0x3;

			objects.add(new GameObject(obj.originalId, newX, newY, chunk.pointZ, obj.getType(), newDirection));
		}
	}

	/**
	 * Build by bounds (May not be precise if bounds coordinates don't match absolute chunk coordinates)
	 */

	public DynamicMap build(Bounds bounds) throws DynamicMapBuildException {
		int xChunks = (int) (Math.ceil((bounds.neX - bounds.swX) / 8) + 1);
		int yChunks = (int) Math.ceil((bounds.neY - bounds.swY) / 8) + 1;
		if (xChunks > 16 || yChunks > 16) {
			throw new IllegalArgumentException("Dynamic regions cannot exceed 2x2 region dimensions");
		}
		List<DynamicChunk> chunks = new ObjectArrayList<>();
		for (int pointZ = 0; pointZ < 4; pointZ++) {
			if (bounds.z != -1 && pointZ != bounds.z) // ignore this height
				continue;
			for (int pointX = 0; pointX < xChunks; pointX++) {
				int chunkX = (bounds.swX + (pointX * 8)) >> 3;
				for (int pointY = 0; pointY < yChunks; pointY++) {
					int chunkY = (bounds.swY + (pointY * 8)) >> 3;
					chunks.add(new DynamicChunk(chunkX, chunkY, pointZ).pos(pointX, pointY, pointZ));
				}
			}
		}
		return build(chunks);
	}

	/**
	 * Events
	 */

	private List<Event> events;

	public void addEvent(EventConsumer eventConsumer) {

		if (events == null) {
			events = new ObjectArrayList<>();
			World.startEvent(event -> {
				while (swRegion != null) {
					for (var evt : events) {
						fork(evt);
					}
					events.clear();
					event.delay(1);
				}
			});
		}

		events.add(EventWorker.createEvent(eventConsumer));
	}

	/**
	 * Npcs
	 */

	private List<NPC> npcs;

	public void addNpc(NPC npc) {
		if (npcs == null)
			npcs = new ObjectArrayList<>();
		npcs.add(npc);
	}

	public void removeNpc(NPC npc) {
		npc.remove();
		npcs.remove(npc);
	}

	public List<NPC> getNpcs() {
		return npcs;
	}

	public void forPlayers(Consumer<Player> action) {
		for (Region r : getRegions()) {
			if (r != null) {
				r.players.forEach(action);
			}
		}
	}

	public boolean containsPlayer(Player player) {
		for (var region : getRegions()) {
			if (player.getPosition().inBoundsWithoutPlane(region.getBounds())) {
				return true;
			}
		}
		return false;
	}

	@Getter
	private List<GameObject> spawnedObjects;

	public void addSpawnedObject(GameObject obj) {
		if (spawnedObjects == null)
			spawnedObjects = new ObjectArrayList<>();
		spawnedObjects.add(obj);
	}

	public void removeSpawnedObject(GameObject obj) {
		spawnedObjects.remove(obj);
	}

	/**
	 * Destroy
	 */

	public final void destroy() {
		try {
			if (npcs != null && !getNpcs().isEmpty()) {
				getNpcs().forEach(NPC::remove);
				getNpcs().clear();
			}

			// Clean up regions in order
			if (swRegion != null) {
				swRegion.destroy();
				if (!FREE_REGIONS.offer(swRegion)) {
					log.error("Unable to set region to reusable again during destroy! id {}", swRegion.id);
				}
				swRegion = null;
			}

			if (nwRegion != null) {
				nwRegion.destroy();
				nwRegion = null;
			}
			if (seRegion != null) {
				seRegion.destroy();
				seRegion = null;
			}
			if (neRegion != null) {
				neRegion.destroy();
				neRegion = null;
			}

		} catch (Exception e) {
			log.error("dmap error", e);
		} finally {
			DynamicMapGuard.unregister(this);
		}
	}

	/**
	 * Convert - Returns the equivalent abs coordinate from the new "dynamic" region.
	 */

	public int convertX(int absX) {
		return convertX(swRegion, absX);
	}

	public int convertY(int absY) {
		return convertY(swRegion, absY);
	}

	public int convertX(Region region, int absX) {
		int localX = absX - region.dynamicRegionBaseX;
		return region.baseX + localX;
	}

	public int convertY(Region region, int absY) {
		int localY = absY - region.dynamicRegionBaseY;
		return region.baseY + localY;
	}

	public Position convertPosition(Position pos) {
		return new Position(convertX(pos.getX()), convertY(pos.getY()), pos.getZ());
	}

	/**
	 * Revert - Returns the equivalent abs coordinate from the src "original" region.
	 */

	public int revertX(int absX) {
		return revertX(swRegion, absX);
	}

	public int revertY(int absY) {
		return revertY(swRegion, absY);
	}

	public int revertX(Region region, int absX) {
		int localX = absX - region.baseX;
		return region.dynamicRegionBaseX + localX;
	}

	public int revertY(Region region, int absY) {
		int localY = absY - region.baseY;
		return region.dynamicRegionBaseY + localY;
	}

	public Position revertPosition(Position pos) {
		return new Position(revertX(pos.getX()), revertY(pos.getY()), pos.getZ());
	}

	/**
	 * Misc
	 */

	public boolean isIn(Player player) {
		int regionId = player.lastRegion.id;
		if (swRegion != null && swRegion.id == regionId)
			return true;
		if (nwRegion != null && nwRegion.id == regionId)
			return true;
		if (seRegion != null && seRegion.id == regionId)
			return true;
		if (neRegion != null && neRegion.id == regionId)
			return true;
		return false;
	}

	public MapListener assignListener(Player player) {
		MapListener listener = toListener();
		player.registerMapListener(listener);
		return listener;
	}

	public MapListener toListener() {
		return new MapListener(this::isIn);
	}

	public Region[] getRegions() {
		return Stream.of(swRegion, seRegion, nwRegion, neRegion)
				.filter(Objects::nonNull).toList().toArray(new Region[0]);
	}

	private Consumer<GameObject> objectConsumer;

	public void setObjectConsumer(Consumer<GameObject> objectConsumer) {
		this.objectConsumer = objectConsumer;
	}

	/**
	 * Load
	 */

	public static void load() {
		List<Region> emptyRegions = new ObjectArrayList<>();
		int dynamicIndex = 1;
		for (int x = 1; x < 127; x++) {
			yLoop: for (int y = 1; y < 255; y++) {
				Region swRegion = Region.get((x * 256) + y);
				if (swRegion == null)
					continue;

				if (Wilderness.getLevel(new Position(swRegion.baseX, swRegion.baseY, 0)) != 0 ||
						Wilderness.getLevel(new Position(swRegion.baseX + 63, swRegion.baseY + 63, 0)) != 0)
					continue;
				int[] regionIds = {swRegion.id, swRegion.id + 1, swRegion.id + 256, swRegion.id + 257};
				/**
				 * Validate regions..
				 */
				for (int regionId : regionIds) {
					Region region = Region.get(regionId);
					if (!region.empty || region.tiles != null) {
						/* this region isn't blank! */
						continue yLoop;
					}
					if (region.dynamicIndex != -1) {
						/* this region is already being used */
						continue yLoop;
					}
				}
				/**
				 * Check passed, queue base (sw) region!
				 */
				for (int regionId : regionIds)
					Region.get(regionId).dynamicIndex = dynamicIndex;
				emptyRegions.add(swRegion);
				dynamicIndex++;
			}
		}

		emptyRegions.removeIf(r -> r.dynamicData != null);

		Collections.shuffle(emptyRegions);
		FREE_REGIONS.addAll(emptyRegions);
		STARTING_FREE_REGIONS = FREE_REGIONS.size();
	}

	/// Whenever build fails for region, its not really usable and should be
	/// destroyed.
	private void buildFailed(Exception reason) throws DynamicMapBuildException {
		this.destroy();
		var error = new DynamicMapBuildException(reason);
		log.error("DMAP build failed", error);
		throw error;
	}

	/// Whenever build fails for region, its not really usable and should be
	/// destroyed.
	private void buildFailed(String reason) throws DynamicMapBuildException {
		this.destroy();
		var error = new DynamicMapBuildException(reason);
		log.error("DMAP build failed", error);
		throw error;
	}

	public DynamicMap persistent(boolean b) {
		this.persistentMap = b;
		return this;
	}

	public boolean hasPlayers() {
		for (var region : this.getRegions()) {
			if (!region.players.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Marks all tiles within all regions of the dynamic map as multi-combat tiles.
	 * <p>
	 * This method iterates through each region, then through each tile within the region, and sets the `multi` property
	 * of each tile to `true`, indicating that multi-combat is enabled for those tiles. Tiles that are null are skipped
	 * during this process.
	 * <p>
	 * Multi-combat areas are areas where multiple entities (e.g., NPCs, players) can engage in combat at the same time
	 * without restrictions typically found in single-combat areas.
	 */
	public void makeDynamicMapMultiCombat() {
		for (Region region : this.getRegions()) {
			for (int z = 0; z < 4; z++) {
				for (int x = 0; x < 64; x++) {
					for (int y = 0; y < 64; y++) {
						Tile tile = Tile.get(region.baseX + x, region.baseY + y, z, true);
						if (tile != null)
							tile.multi = true;
					}
				}
			}
		}
	}

	/**
	 * Copies NPC spawns from the original ("real") map regions to the current dynamic map.
	 * <p>
	 * This method extracts region IDs from the dynamic map's configured regions and filters NPCs that belong to these
	 * regions. Filtered NPCs are then added to the dynamic map's NPC collection. It utilizes the `npcsSlots` stream from
	 * the `World` class to iterate through all available NPCs and checks their positions against the extracted region
	 * IDs.
	 * <p>
	 * The procedure ensures that only NPCs whose positions match the target regions are included in the dynamic map.
	 */
	public void copyRealMapNpcSpawnsToDynamicMap(List<Integer> regions) {
		// Copy main world spawns
		var npcsInRegion = new ObjectArrayList<NPC>();
		World.npcsSlots()
				.forEach(npc -> {
					regions.stream()
							.mapToInt(region -> region)
							.filter(region -> npc.getPosition().getRegion().id == region)
							.mapToObj(region -> npc)
							.forEach(npcsInRegion::add);
				});
		npcsInRegion.forEach(npc -> {
			var cachedNpc = new NPC(npc.getId())
					.spawn(convertPosition(npc.getSpawnPosition()));
			addNpc(cachedNpc);
		});
	}

}
