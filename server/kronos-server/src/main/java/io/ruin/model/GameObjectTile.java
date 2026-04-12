package io.ruin.model;

import io.ruin.api.utils.RsAttributesHolder;
import io.ruin.model.map.Position;

/**
 * so java code can obj.x without parenthesis of getters, and kotlin code can
 * point to a underlying field tile:Position
 *
 * @author shadowrs
 */
public abstract class GameObjectTile extends RsAttributesHolder {
	protected transient final Position tile;

	public transient int x;
	public transient int y;
	public transient int z;

	public GameObjectTile(Position tile) {
		this.tile = tile;
		this.x = tile.getX();
		this.y = tile.getY();
		this.z = tile.getZ();
	}

	public GameObjectTile(int x, int y, int z) {
		this(new Position(x, y, z));
	}

	public int getX() {
		return tile.getX();
	}

	public int getY() {
		return tile.getY();
	}

	public int getZ() {
		return tile.getZ();
	}

	public Position pos() {
		return new Position(this.x, this.y, this.z);
	}
}
