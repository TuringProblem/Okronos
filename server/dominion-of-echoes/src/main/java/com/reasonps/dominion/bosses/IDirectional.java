package com.reasonps.dominion.bosses;

import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-25
 */
public interface IDirectional {

	default List<Position> getTiles(Position target, Position source) {
		var direction = Direction.getDirection(source, target);
		var distance = source.distance(target);
		var tiles = switch (direction) {
			case WEST, EAST -> getHorizontalTiles(direction, source, distance);
			case NORTH, SOUTH -> getVerticalTiles(direction, source, distance);
			case NORTH_WEST, NORTH_EAST, SOUTH_WEST, SOUTH_EAST -> getDiagonalTiles(direction, source, distance);

		};
		return new ArrayList<>(tiles);
	}

	default List<Position> getHorizontalTiles(Direction direction, Position source, int maxDistance) {
		return IntStream.range(0, maxDistance + 1)
			.mapToObj(i -> source.translated(getOffsetForX(direction, i), 0))
			.collect(Collectors.toCollection(ArrayList::new));
	}

	default List<Position> getVerticalTiles(Direction direction, Position source, int maxDistance) {
		return IntStream.range(0, maxDistance)
			.mapToObj(i -> source.translated(0, getOffsetForY(direction, i)))
			.collect(Collectors.toCollection(ArrayList::new));
	}

	default List<Position> getDiagonalTiles(Direction direction, Position source, int maxDistance) {
		return IntStream.range(0, maxDistance)
			.mapToObj(i -> source.translated(getOffsetForDiagonalX(direction, i), getOffsetForDiagonalY(direction, i)))
			.collect(Collectors.toCollection(ArrayList::new));
	}


	default int getOffsetForX(Direction direction, int i) {
		return direction == Direction.WEST ? -i : direction == Direction.EAST ? i : 0;
	}

	default int getOffsetForY(Direction direction, int i) {
		return direction == Direction.NORTH ? i : direction == Direction.SOUTH ? -i : 0;
	}

	default int getOffsetForDiagonalX(Direction direction, int i) {
		return switch (direction) {
			case NORTH_WEST, SOUTH_WEST -> -i;
			case NORTH_EAST, SOUTH_EAST -> i;
			default -> 0;
		};
	}

	default int getOffsetForDiagonalY(Direction direction, int i) {
		return switch (direction) {
			case NORTH_WEST, NORTH_EAST -> i;
			case SOUTH_WEST, SOUTH_EAST -> -i;
			default -> 0;
		};
	}

	default int getPerpendicularOffsetX(Direction direction) {
		return switch (direction) {
			case NORTH_WEST, SOUTH_EAST, NORTH_EAST, SOUTH_WEST -> -1;
			default -> 0;
		};
	}

	default int getPerpendicularOffsetY(Direction direction) {
		return switch (direction) {
			case NORTH_WEST, SOUTH_EAST -> -1;
			case NORTH_EAST, SOUTH_WEST -> 1;
			default -> 0;
		};
	}

	default Direction getDirection(Position src, Position dest) {
		// Center of the NPC's area (assuming a 5x5 area for the center calculation)
		int npcCenterX = src.getX() + 2;
		int npcCenterY = src.getY() + 2;

		// Calculate differences
		int deltaX = dest.getX() - npcCenterX;
		int deltaY = dest.getY() - npcCenterY;

		// Calculate absolute values
		int absDeltaX = Math.abs(deltaX);
		int absDeltaY = Math.abs(deltaY);

		// Determine primary direction
		if (deltaX == -2 && deltaY == 2) return Direction.NORTH;
		if (deltaX == -1 && deltaY == 2) return Direction.NORTH;
		if (deltaX == 1 && deltaY == -1) return Direction.NORTH_EAST;
		if (deltaX == 2 && deltaY == 1) return Direction.NORTH_EAST;
		if (deltaX == -4 && deltaY == 1) return Direction.NORTH_WEST;
		if (deltaX == -5 && deltaY == 1) return Direction.NORTH_WEST;
		if (deltaX == -5 && deltaY == 0) return Direction.NORTH_WEST;
		if (deltaX == -6 && deltaY == 0) return Direction.NORTH_WEST;
		if (deltaX == -6 && deltaY == -5) return Direction.SOUTH_WEST;
		if (deltaX == -5 && deltaY == -6) return Direction.SOUTH_WEST;
		if (deltaX == -4 && deltaY == -5) return Direction.SOUTH_WEST;
		if (deltaX == -6 && deltaY == -6) return Direction.SOUTH_WEST;
		if (deltaX == -5 && deltaY == -4) return Direction.SOUTH_WEST;
		if (deltaX == -6 && deltaY == -4) return Direction.SOUTH_WEST;
		if (deltaX == -5 && deltaY == -5) return Direction.SOUTH_WEST;
		if (deltaX == 0 && deltaY == -5) return Direction.SOUTH_EAST;
		if (deltaX == 1 && deltaY == -6) return Direction.SOUTH_EAST;
		if (deltaX == 1 && deltaY == -5) return Direction.SOUTH_EAST;
		if (deltaX == 2 && deltaY == -6) return Direction.SOUTH_EAST;
		if (deltaX == 1 && deltaY == -4) return Direction.SOUTH_EAST;
		if (deltaX == 2 && deltaY == -5) return Direction.SOUTH_EAST;
		if (deltaX == 2 && deltaY == -4) return Direction.SOUTH_EAST;
		if (deltaX == 0 && deltaY == -6) return Direction.SOUTH_EAST;
		if (deltaX == -5 && deltaY == 2) return Direction.NORTH_WEST;
		if (deltaX == -6 && deltaY == 1) return Direction.NORTH_WEST;
		if (deltaX == -6 && deltaY == 2) return Direction.NORTH_WEST;
		if (deltaX == -4 && deltaY == 2) return Direction.NORTH_WEST;
		if (deltaX == 2 && deltaY == 2) return Direction.NORTH_EAST;
		if (deltaX == 1 && deltaY == 0) return Direction.NORTH_EAST;
		if (deltaX == 1 && deltaY == 2) return Direction.NORTH_EAST;
		if (deltaX == 0 && deltaY == 1) return Direction.NORTH_EAST;
		if (deltaX == 0 && deltaY == 2) return Direction.NORTH_EAST;
		if (deltaX == 1 && deltaY == 1) return Direction.NORTH_EAST;
		if (deltaX == 2 && deltaY == 0) return Direction.NORTH_EAST;
		if (deltaX == 2 && deltaY == -2) return Direction.EAST;
		if (absDeltaX == 1 && absDeltaY == 1) return Direction.NORTH;
		if (absDeltaX == 1 && absDeltaY == 2) return Direction.EAST;
		if (absDeltaX == 2 && absDeltaY == 1) return Direction.NORTH;
		if (absDeltaX == 2 && absDeltaY == 3) return Direction.EAST;
		if (absDeltaX == 1 && absDeltaY == 3) return Direction.EAST;
		if (absDeltaX >= 1 && absDeltaX <= 4 && absDeltaY >= 1 && absDeltaY < 3) return Direction.NORTH;
		// Horizontal movement
		if (absDeltaX > absDeltaY) if (deltaX > 0) return Direction.EAST;
		else return Direction.WEST;
		else // Handle diagonal cases if distances are equal
			// Vertical movement
			if (absDeltaY > absDeltaX) if (deltaY > 0) return Direction.NORTH;
			else return Direction.SOUTH;
			else if (deltaX > 0 && deltaY > 0) return Direction.SOUTH_EAST;
			else if (deltaX > 0 && deltaY < 0) return Direction.NORTH_EAST;
			else if (deltaX < 0 && deltaY > 0) return Direction.SOUTH_WEST;
			else if (deltaX < 0 && deltaY < 0) return Direction.NORTH_WEST;
		// Default to a safe direction if none of the above matches
		return Direction.NORTH;
	}
}
