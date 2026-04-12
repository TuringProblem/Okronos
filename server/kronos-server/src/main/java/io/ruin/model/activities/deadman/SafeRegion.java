package io.ruin.model.activities.deadman;

import io.ruin.model.map.Tile;

import java.util.HashSet;

public class SafeRegion {

	// Is the entire region a safe zone
	public boolean entireRegionSafe;

	// Chunk IDs which are safe
	public HashSet<LeveledChunk> safeChunks = new HashSet<>();

	// Tiles which are safe
	public HashSet<Tile> safeTiles = new HashSet<>();

	// A list of tiles that are dangerous. Even if a chunk is 'safe' we will check this list
	// and if our current position matches an override, it will be dangerous.
	public HashSet<Tile> dangerousTileOverrides = new HashSet<>();

	// Even if in a safe region or chunk, if we're in one of these area overrides, it will be dangerous.
	//  public HashSet<Area> dangerousAreaOverrides = new HashSet<>();//todo fix

	// Even if our current Tile is in a Safe zone - is that tile an override which is actually dangerous?
	public boolean excluded(Tile pos) {
		for (Tile t : dangerousTileOverrides) {
			if (pos.equals(t)) return true; // (has internal level check)
		}
        /*for (Area a : dangerousAreaOverrides) {//todo fix
            if (a.contains(pos) && a.level == pos.level) return true;
        }*/
		return false;
	}
}