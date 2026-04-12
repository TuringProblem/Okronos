package io.ruin.model.map;

import io.ruin.model.item.actions.impl.AchievementLamp;

public enum Direction {

	/**
	 * DO NOT REORDER
	 */

	NORTH_WEST(-1, 1, 768),     //0
	NORTH(0, 1, 1024),          //1
	NORTH_EAST(1, 1, 1280),     //2
	WEST(-1, 0, 512),           //3
	EAST(1, 0, 1536),           //4
	SOUTH_WEST(-1, -1, 256),    //5
	SOUTH(0, -1, 0),            //6
	SOUTH_EAST(1, -1, 1792);    //7

	public final int deltaX, deltaY;

	public final int clientValue;

	Direction(int deltaX, int deltaY, int clientValue) {
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		this.clientValue = clientValue;
	}

	public static final Direction[] VALUES = values();

	public static Direction get(String cardinal) {
		return switch (cardinal.toUpperCase()) {
			case "N" -> Direction.NORTH;
			case "NW" -> Direction.NORTH_WEST;
			case "NE" -> Direction.NORTH_EAST;
			case "W" -> Direction.WEST;
			case "E" -> Direction.EAST;
			case "SW" -> Direction.SOUTH_WEST;
			case "SE" -> Direction.SOUTH_EAST;
			default -> Direction.SOUTH;
		};
	}

	public static Direction getFromObjectDirection(int direction) {
		return switch (direction) {
			case 1 -> WEST;
			case 2 -> NORTH;
			case 3 -> EAST;
			default -> SOUTH;
		};
	}

	public static Direction fromDoorDirection(int direction) {
		return switch (direction) {
			case 0 -> WEST;
			case 1 -> NORTH;
			case 2 -> EAST;
			default -> SOUTH;
		};
	}

	public static Direction getDirection(Position src, Position dest) {
		int deltaX = dest.getX() - src.getX();
		int deltaY = dest.getY() - src.getY();
		return getDirection(deltaX, deltaY);
	}

	public static Direction getDirection(int deltaX, int deltaY) {
		if (deltaX != 0)//normalize
			deltaX /= Math.abs(deltaX);
		if (deltaY != 0)
			deltaY /= Math.abs(deltaY);
		for (Direction d : Direction.VALUES) {
			if (d.deltaX == deltaX && d.deltaY == deltaY)
				return d;
		}
		return SOUTH;
	}

	public static Direction getDirection2(int deltaX, int deltaY) {
		// Normalize deltaX and deltaY
		if (deltaX != 0)
			deltaX = deltaX / Math.abs(deltaX);
		if (deltaY != 0)
			deltaY = deltaY / Math.abs(deltaY);

		// Check if the normalized deltaX and deltaY match any Direction enum values
		for (Direction d : Direction.VALUES) {
			if (d.deltaX == deltaX && d.deltaY == deltaY)
				return d;
		}
		return null;
	}


}