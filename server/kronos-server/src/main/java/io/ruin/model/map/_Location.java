package io.ruin.model.map;

/**
 * from NR
 */
public class _Location {

	public transient int hash;

	public _Location(final int hash) {
		this.hash = hash;
	}

	public _Location(final int x, final int y, final int z) {
		this(y | x << 14 | z << 28);
	}

	public static int getRegionId(final int x, final int y) {
		return _Location.getRegionIDByRegion(_Location.getRegionX(x), _Location.getRegionY(y));
	}

	public static int getRegionX(final int x) {
		return x >> 6;
	}

	public static int getRegionY(final int y) {
		return y >> 6;
	}

	public static int getRegionIDByRegion(final int regionX, final int regionY) {
		return (regionX << 8) | regionY;
	}

	public int getX() {
		return (hash >> 14) & 16383;
	}

	public int getY() {
		return hash & 16383;
	}

	public int getPlane() {
		return (hash >> 28) & 3;
	}

	public int getZ() {
		return (hash >> 28) & 3;
	}

	public int getChunkX() {
		return (getX() >> 3);
	}

	public int getChunkY() {
		return (getY() >> 3);
	}

	public int getXInRegion() {
		return getX() & 63;
	}

	public int getYInRegion() {
		return getY() & 63;
	}

	public int getXInChunk() {
		return getX() & 7;
	}

	public int getYInChunk() {
		return getY() & 7;
	}

	public int getPositionHash() {
		return hash;
	}

	@Override
	public int hashCode() {
		return getPositionHash();
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof _Location)) {
			return false;
		}
		return ((_Location) other).getPositionHash() == getPositionHash();
	}

	public int getRegionX() {
		return _Location.getRegionX(getX());
	}

	public int getRegionY() {
		return _Location.getRegionY(getY());
	}

	public int getRegionId() {
		return _Location.getRegionId(getX(), getY());
	}

	@Override
	public String toString() {
		return "Tile: " + getX() + ", " + getY() + ", " + getPlane() + ", region[" + getRegionId() + ", " + getRegionX()
			+ ", " + getRegionY() + "], chunk[" + getChunkX() + ", " + getChunkY() + "], hash [" + getPositionHash() + "]";
	}

	public int region() {
		return getRegionId();
	}

	public int getChunkHash() {
		return getChunkX() | getChunkY() << 11 | (getPlane() & 3) << 22;
	}

}
