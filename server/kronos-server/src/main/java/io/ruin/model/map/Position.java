package io.ruin.model.map;

import io.ruin.cache.ItemID;
import io.ruin.model.map.ground.GroundItem;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
public class Position extends _Location {

	public Position(int x, int y, int z) {
		super(x, y, z);
	}

	public Position(int x, int y) {
		super(x, y, 0);
	}

	public Position(Position other) {
		super(other.x(), other.y(), other.z());
	}

	public static Position of(int x, int y) {
		return new Position(x, y, 0);
	}

	public static Position of(int x, int y, int z) {
		return new Position(x, y, z);
	}

	public Position copy() {
		return new Position(getX(), getY(), getZ());
	}

	public void set(Position other) {
		setLocation(other.getX(), other.getY(), other.getZ());
	}

	public void set(int x, int y) {
		setLocation(x, y, getZ());
	}

	public void set(int x, int y, int z) {
		setLocation(x, y, z);
	}

	public Position moveLocation(final int xOffset, final int yOffset, final int planeOffset) {
		int x = getX();
		int y = getY();
		int z = getPlane();
		x += xOffset;
		y += yOffset;
		z += planeOffset;
		hash = y | x << 14 | z << 28;
		return this;
	}

	public void setLocation(final int x, final int y, final int plane) {
		hash = y | x << 14 | plane << 28;
	}

	public Position translate(int changeX, int changeY, int changeZ) {
		moveLocation(changeX, changeY, changeZ);
		return this;
	}

	public Position translate(int changeX, int changeY) {
		moveLocation(changeX, changeY, 0);
		return this;
	}

	public Position translated(int changeX, int changeY, int changeZ) {
		return new Position(getX() + changeX, getY() + changeY, getZ() + changeZ);
	}

	public Position translated(int changeX, int changeY) {
		return new Position(getX() + changeX, getY() + changeY, getZ());
	}

	public int getDistance(Position other) {
		return (int) Math.sqrt(
				Math.pow(getX() - other.getX(), 2) + Math.pow(getY() - other.getY(), 2) + Math.pow(getZ() - other.getZ(), 2));
	}

	public Position relative(int changeX, int changeY, int changeZ) {
		return copy().translate(changeX, changeY, changeZ);
	}

	public Position relative(int changeX, int changeY) {
		return relative(changeX, changeY, 0);
	}

	public int getChunkX() {
		return getX() >> 3;
	}

	public int getChunkY() {
		return getY() >> 3;
	}

	public int regionX() {
		var region = this.region();
		var regionX = (region >> 8 & 0xff) * 64;
		return getX() - regionX;
	}

	public int regionY() {
		var region = this.region();
		var regionY = (region & 0xff) * 64;
		return getY() - regionY;
	}

	public int getLocalX() {
		return getX() - 8 * (getChunkX() - 6);
	}

	public int getLocalY() {
		return getY() - 8 * (getChunkY() - 6);
	}

	public int getSceneX() {
		return getX() - ((getChunkX() - 6) << 3);
	}

	public int getSceneY() {
		return getY() - ((getChunkY() - 6) << 3);
	}

	public int getLocalX(int baseRegionX) {
		return getX() - 8 * (baseRegionX - 6);
	}

	public int getLocalY(int baseRegionY) {
		return getY() - 8 * (baseRegionY - 6);
	}

	public static int getLocal(int abs, int chunk) {
		return abs - 8 * (chunk - 6);
	}

	public int getTileHash() {
		return getY() + (getX() << 14) + (getZ() << 28);
	}

	public int getRegionHash() {
		return (getY() >> 13) + ((getX() >> 13) << 8) + (getZ() << 16);
	}

	public Region getRegion() {
		return Region.get(getX(), getY());
	}

	public int regionId() {
		return ((getX() >> 6) << 8 | getY() >> 6);
	}

	public Tile getTile() {
		return Tile.get(getX(), getY(), getZ(), true);
	}

	public boolean isWithinDistance(Position other) {
		return isWithinDistance(other, 16);
	}

	public boolean isWithinDistance(Position other, int distance) {
		return isWithinDistance(other, true, distance);
	}

	public boolean isWithinDistance(Position other, boolean checkHeight, int distance) {
		return (!checkHeight || other.getZ() == getZ()) && Math.abs(getX() - other.getX()) <= distance
				&& Math.abs(getY() - other.getY()) <= distance;
	}

	public boolean inBounds(Bounds bounds) {
		return bounds.inBounds(getX(), getY(), getZ(), 0);
	}

	public boolean inBoundsWithoutPlane(Bounds bounds) {
		return bounds.inBoundsWithoutPlane(getX(), getY(), 0);
	}

	public boolean inBounds(Bounds bounds, int range) {
		return bounds.inBounds(getX(), getY(), getZ(), range);
	}

	public boolean equals(int x, int y) {
		return this.getX() == x && this.getY() == y;
	}

	public boolean equals(int x, int y, int z) {
		return this.getX() == x && this.getY() == y && this.getZ() == z;
	}

	public boolean equals(Position pos) {
		return pos.getX() == getX() && pos.getY() == getY() && pos.getZ() == getZ();
	}

	public Position centerOf(int size) {
		return Position.of(getCoordFaceX(size), getCoordFaceY(size), getZ());
	}

	public int getCoordFaceX(int sizeX) {
		return getCoordFaceX(sizeX, -1, -1);
	}

	public int getCoordFaceX(int sizeX, int sizeY, int rotation) {
		return getX() + ((rotation == 1 || rotation == 3 ? sizeY : sizeX) - 1) / 2;
	}

	public int getCoordFaceX(int x, int sizeX, int sizeY, int rotation) {
		return x + ((rotation == 1 || rotation == 3 ? sizeY : sizeX) - 1) / 2;
	}

	public int getCoordFaceY(int sizeY) {
		return getCoordFaceY(-1, sizeY, -1);
	}

	public int getCoordFaceY(int sizeX, int sizeY, int rotation) {
		return getY() + ((rotation == 1 || rotation == 3 ? sizeX : sizeY) - 1) / 2;
	}

	public int getCoordFaceY(int y, int sizeX, int sizeY, int rotation) {
		return y + ((rotation == 1 || rotation == 3 ? sizeX : sizeY) - 1) / 2;
	}

	public List<Position> area(int radius, Predicate<Position> filter) {
		List<Position> list = new ArrayList<>((int) Math.pow((1 + radius), 2));
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= +radius; y++) {
				Position pos = relative(x, y);
				if (filter.test(pos))
					list.add(pos);
			}
		}
		return list;
	}

	public List<Position> area(int radius) {
		return area(radius, p -> true);
	}

	public int unitVectorX(Position target) {
		int diff = target.getX() - getX();
		if (diff != 0)
			diff /= Math.abs(diff);
		return diff;
	}

	public int unitVectorY(Position target) {
		int diff = target.getY() - getY();
		if (diff != 0)
			diff /= Math.abs(diff);
		return diff;
	}

	@Override
	public String toString() {
		return "[x=" + getX() + ", y=" + getY() + ", z=" + getZ() + "]";
	}

	public Position localPosition() { // this feels really wrong but i dont wanna create another class...
		return new Position(getX() & 63, getY() & 63, getZ());
	}

	public boolean isAtPosition(Position other) {
		return distance(other) == 0;
	}

	public int distance(Position position) {
		int dx = position.getX() - getX();
		int dz = position.getY() - getY();
		return (int) Math.sqrt(dx * dx + dz * dz);
	}

	public int x() {
		return getX();
	}

	public int y() {
		return getY();
	}

	public int z() {
		return getZ();
	}

	public int plane() {
		return getZ();
	}

	public int component1() {
		return getX();
	}

	public int component2() {
		return getY();
	}

	public int component3() {
		return getZ();
	}

	public Position center(int size) {
		return translate((int) Math.ceil(size / 2.0), (int) Math.ceil(size / 2.0), 0);
	}

	public Position centrePosition(int size) {
		return new Position(getX() + (int) Math.floor(size / 2.0), getY() + (int) Math.ceil(size / 3.0), 0);
	}

	public GroundItem spawnTempItem() {
		return new GroundItem(ItemID.VIAL, 1).position(this).spawnPrivate();
	}
}
