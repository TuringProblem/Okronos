package io.ruin.model.activities.bosses.instancetoken;

import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import io.ruin.model.map.polly.RSPolygon;
import io.ruin.utility.Random;

import java.util.stream.IntStream;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-06-06
 */
public abstract class PolygonRegionArea extends MapHandler {

	protected RSPolygon[] polygons;

	protected abstract RSPolygon[] polygons();

	/**
	 * Retrieves the polygons defining the current region.
	 * If the polygons have not been initialized, they will be generated
	 * by calling the abstract {@code polygons()} method.
	 *
	 * @return an array of {@link RSPolygon} objects representing the polygons
	 *         defining the region, or {@code null} if no polygons are defined
	 */
	public RSPolygon[] getPolygons() {
		if (polygons == null)
			polygons = polygons();
		return polygons;
	}

	/**
	 * Determines whether the given location is within the bounds of any of the polygons
	 * defining the current region.
	 *
	 * @param location the {@link Position} to check
	 * @return {@code true} if the location is contained within any polygon in the region,
	 *         {@code false} otherwise
	 */
	public boolean inside(final Position location) {
		final var polygons = getPolygons();
		if (polygons == null || polygons.length == 0)
			return false;

		return IntStream.iterate(polygons.length - 1, i -> i >= 0, i -> i - 1)
			.mapToObj(i -> polygons[i])
			.anyMatch(polygon -> polygon.contains(location));
	}

	/**
	 * Generates a random {@link Position} within the bounds of the polygons defining this region.
	 * The generated position is guaranteed to be inside one of the polygons in the region.
	 *
	 * @return a random {@link Position} that is within the defined polygons of this region
	 */
	public Position getRandomPosition(Player player) {
		var random = Random.get(getPolygons());
		var bottomLeftX = Integer.MAX_VALUE;
		var bottomLeftY = Integer.MAX_VALUE;
		var topRightX = Integer.MIN_VALUE;
		var topRightY = Integer.MIN_VALUE;

		for (int[] values : random.getPoints()) {
			if (values[0] <= bottomLeftX) bottomLeftX = values[0];
			if (values[0] >= topRightX) topRightX = values[0];
			if (values[1] <= bottomLeftY) bottomLeftY = values[1];
			if (values[1] >= topRightY) topRightY = values[1];
		}
		Position randomLoc = null;

		while (randomLoc == null) {
			int x = Random.get(bottomLeftX, topRightX);
			int y = Random.get(bottomLeftY, topRightY);
			var newLoc = Position.of(x, y);
			if (inside(newLoc))
				randomLoc = newLoc;
		}
		if (!randomLoc.isWithinDistance(player.getPosition(), 8))
			return getRandomPosition(player);
		return randomLoc;
	}
}
