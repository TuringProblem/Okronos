package io.ruin.model.map;

import io.ruin.Server;
import io.ruin.api.buffer.InBuffer;
import io.ruin.api.filestore.IndexFile;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.containers.bank.BankActions;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.skills.construction.House;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class Region {
	public static boolean NO_KEYS = true;
	public static final int CLIENT_SIZE = 104;
	public static final Region[] LOADED = new Region[0xFFFF];

	public static final Int2ObjectMap<Region> regions = new Int2ObjectOpenHashMap<>();

	public static Region get(int regionId) {
		return LOADED[regionId];
	}

	public static Region get(Position abs) {
		return get(getId(abs.x(), abs.y()));
	}

	public static Region get(int absX, int absY) {
		return get(getId(absX, absY));
	}

	public static int getId(int absX, int absY) {
		return ((absX >> 6) << 8) | absY >> 6;
	}

	public static int getClipping(int x, int y, int z) {
		Region region = Region.get(x, y);
		if (region.empty)
			return ClipConstants.UNMOVABLE_MASK;
		Tile tile = region.getTile(x, y, z, false);
		return tile == null ? 0 : tile.clipping;
	}

	/**
	 * Updating
	 */

	public static void update(Player player) {
		for (Region region : player.getRegions()) {
			for (Tile tile : region.activeTiles)
				tile.update(player);
		}
	}

	public final int id;
	public final int baseX, baseY;
	public final ObjectArrayList<Player> players;
	public final ObjectArrayList<Tile> activeTiles;
	private House house; // house instance that this region currently belongs to
	public Tile[][][] tiles;
	public int[][][][] dynamicData;
	public int dynamicIndex = -1;
	public int dynamicRegionBaseX, dynamicRegionBaseY;
	public boolean empty;

	public Region(int id) {
		this.id = id;
		this.baseX = (id >> 8) * 64;
		this.baseY = (id & 0xff) * 64;
		this.players = new ObjectArrayList<>(0);
		this.activeTiles = new ObjectArrayList<>(0);
	}

	public Bounds getBounds() {
		return Bounds.fromRegion(id);
	}

	// not my favorite design, let's come back to this one day..
	public void init() {
		short[][][] tileData = new short[4][64][64];
		byte[] mapData = getMapData();
		if (mapData != null) {
			InBuffer mapIn = new InBuffer(mapData);
			for (int z = 0; z < 4; z++) {
				for (int x = 0; x < 64; x++) {
					for (int y = 0; y < 64; y++) {
						for (;;) {
							int i = mapIn.readUnsignedShort() & 0x7FFF;
							if (i == 0)
								break;
							if (i == 1) {
								mapIn.skipByte();
								break;
							}
							if (i <= 49) {
								mapIn.readShort();
							} else if (i <= 81)
								tileData[z][x][y] = (short) (i - 49);
						}
					}
				}
			}
			for (int z = 0; z < 4; z++) {
				for (int x = 0; x < 64; x++) {
					for (int y = 0; y < 64; y++) {
						if ((tileData[z][x][y] & 0x1) == 1) {
							int height = z;
							if ((tileData[1][x][y] & 0x2) == 2)
								height--;
							if (height >= 0) {
								int absX = baseX + x;
								int absY = baseY + y;
								Tile tile = getTile(absX, absY, z, true);
								tile.flagUnmovable();
								tile.defaultClipping = tile.clipping;
							}
						}
						if ((tileData[z][x][y] & 0x4) != 0) {
							int absX = baseX + x;
							int absY = baseY + y;
							Tile tile = getTile(absX, absY, z, true);
							tile.roofExists = true;
						}
					}
				}
			}
		}
		byte[] landscapeData = null;
		try {
			landscapeData = getLandscapeData();
		} catch (Throwable t) {
			System.err.println("Landscape data not found for region: " + this.id);
		}

		if (landscapeData != null) {
			InBuffer landIn = new InBuffer(landscapeData);
			int objectId = -1;
			for (;;) {
				int idOffset = landIn.readUnsignedIntSmartShortCompat();
				if (idOffset == 0)
					break;
				objectId += idOffset;

				int position = 0;
				for (;;) {
					int positionOffset = landIn.readUnsignedIntSmartShortCompat();
					if (positionOffset == 0)
						break;
					position += positionOffset - 1;
					int localY = position & 0x3f;
					int localX = (position >> 6) & 0x3f;
					int height = position >> 12;

					int attributes = landIn.readUnsignedByte();
					int type = attributes >> 2;
					int direction = attributes & 0x3;

					if ((tileData[1][localX][localY] & 0x2) == 2)
						height--;
					if (height >= 0) {
						int absX = baseX + localX;
						int absY = baseY + localY;
						GameObject obj = new GameObject(objectId, absX, absY, height, type, direction);
						getTile(absX, absY, height, true).addObject(obj);
						BankActions.markTiles(obj);
					}
				}
			}
		}
		empty = mapData == null && landscapeData == null;
	}

	public Tile getTile(int x, int y, int z, boolean create) {
		int localX = x - baseX;
		int localY = y - baseY;
		if (tiles == null) {
			if (!create)
				return null;
			tiles = new Tile[4][64][64];
			log.trace("Region {} initialized. {} {} {} {}", id, x, y, z, create/* , new RuntimeException("") */);
		}
		Tile tile = tiles[z][localX][localY];
		if (tile == null && create)
			tile = tiles[z][localX][localY] = new Tile(this);
		return tile;
	}

	public byte[] getLandscapeData() {
		IndexFile index = Server.fileStore.get(5);
		int landArchiveId = index.getArchiveId("l" + ((baseX >> 3) / 8) + "_" + ((baseY >> 3) / 8));
		return landArchiveId == -1 ? null
				: index.getFile(landArchiveId, 0, null);
	}

	/**
	 * Destroy
	 */

	public void destroy() {
		players.clear();
		if (!activeTiles.isEmpty()) {
			for (Tile tile : activeTiles)
				tile.destroy();
			activeTiles.clear();
		}
		if (tiles != null) {
			for (int i = 0; i < tiles.length; i++) {
				if (tiles[i] == null)
					continue;
				for (int i1 = 0; i1 < tiles[i].length; i1++) {
					for (int i2 = 0; i2 < tiles[i][i1].length; i2++) {
						if (tiles[i][i1][i2] == null)
							continue;
						tiles[i][i1][i2].destroy();
						tiles[i][i1][i2] = null;
					}
				}
			}
		}
		tiles = null;
		dynamicIndex = -1;
		// setting array contents to all 0 helps GC identity dead data beyond just
		// setting =null
		if (dynamicData != null) {
			for (int[][][] dynamicDatum : dynamicData) {
				if (dynamicDatum == null)
					continue;
				for (int[][] ints : dynamicDatum) {
					if (ints == null)
						continue;
					for (int[] anInt : ints) {
						if (anInt == null)
							continue;
						Arrays.fill(anInt, 0);
					}
				}
			}
		}
		dynamicData = null;
		empty = true;
		house = null;
	}

	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

	private byte[] getMapData() {
		IndexFile index = Server.fileStore.get(5);
		int mapArchiveId = index.getArchiveId("m" + ((baseX >> 3) / 8) + "_" + ((baseY >> 3) / 8));
		return mapArchiveId == -1 ? null : index.getFile(mapArchiveId, 0);
	}
}
