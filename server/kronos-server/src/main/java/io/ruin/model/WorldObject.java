
package io.ruin.model;

import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;

/**
 * @author Jire
 */
public class WorldObject extends GameObjectTile {

	private transient int objectKey;

	private WorldObject(int x, int y, int plane, int objectKey) {
		super(x, y, plane);
		this.objectKey = objectKey;
	}

	public WorldObject(int id, int type, int rotation, int x, int y, int plane) {
		this(x, y, plane, objectKey(id, type, rotation, false));
	}

	public WorldObject(int id, int type, int rotation, Position tile) {
		this(tile.getX(), tile.getY(), tile.getPlane(), objectKey(id, type, rotation, false));
	}

	public WorldObject(WorldObject from) {
		this(from.getX(), from.getY(), from.getZ(), from.objectKey);
	}

	public int getId() {
		return id(objectKey);
	}

	public void setId(int id) {
		objectKey = objectKey(id, type(), direction(), isLocked());
	}

	public int type() {
		return type(objectKey);
	}

	public int getType() {
		return type(objectKey);
	}

	public void setType(int type) {
		objectKey = objectKey(id(), type, direction(), isLocked());
	}

	public int id() {
		return id(objectKey);
	}

	public int direction() {
		return rotation(objectKey);
	}

	public int getDirection() {
		return rotation(objectKey);
	}

	public void setDirection(int rotation) {
		objectKey = objectKey(id(), type(), rotation, isLocked());
	}

	public Direction getFaceDirection() {
		switch (direction()) {
			case 0:
				return Direction.WEST;
			case 1:
				return Direction.NORTH;
			case 2:
				return Direction.EAST;
			default:
				return Direction.SOUTH;
		}
	}

	public boolean isLocked() {
		return locked(objectKey);
	}

	public void setLocked(boolean isLocked) {
		objectKey = objectKey(id(), type(), direction(), isLocked);
	}

	public WorldObject transform(int newID) {
		var obj = new WorldObject(this);
		obj.setId(newID);
		return obj;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof WorldObject))
			return false;
		WorldObject that = (WorldObject) other;
		return this.objectKey == that.objectKey && this.tile.hashCode() == that.tile.hashCode();
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + objectKey;
		return result;
	}

	public static final int DEFAULT_TYPE = 10;
	public static final int DEFAULT_ROTATION = 0;

	public static int objectKey(int id, int type, int rotation, boolean locked) {
		return (id & 0xFF_FF_FF) |
			((type & 0b1_1111) << 24) |
			((rotation & 0b11) << 29) |
			((locked ? 1 : 0) << 31);
	}

	public static int id(int objectKey) {
		return objectKey & 0xFF_FF_FF;
	}

	public static int type(int objectKey) {
		return (objectKey >>> 24) & 0b1_1111;
	}

	public static int rotation(int objectKey) {
		return (objectKey >>> 29) & 0b11;
	}

	public static boolean locked(int objectKey) {
		return ((objectKey >>> 31) & 1) == 1;
	}
}
