package io.ruin.model.map.polly;

import io.ruin.api.utils.Random;
import io.ruin.model.map.Position;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;

import java.util.BitSet;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-06-06
 */
public class RSPolygon {
	@Getter private final Polygon polygon;
	@Getter private final int[] planes;
	@Getter private final int[][] points;

	private final BitSet planeSet = new BitSet(4);

	/**
	 * Constructs an RSPolygon object by defining a rectangular polygon using the southwest
	 * and northeast corner coordinates.
	 *
	 * @param swX the x-coordinate of the southwest corner of the polygon.
	 * @param swY the y-coordinate of the southwest corner of the polygon.
	 * @param neX the x-coordinate of the northeast corner of the polygon.
	 * @param neY the y-coordinate of the northeast corner of the polygon.
	 */
	public RSPolygon(int swX, int swY, int neX, int neY) {
		this(new int[][] {
			{ swX, swY },
			{ neX, swY },
			{ neX, neY },
			{ swX, neY }
		});
	}

	/**
	 * Constructs an RSPolygon object using a 2D array of coordinates.
	 *
	 * @param points a 2D array where each element represents a vertex of the polygon.
	 *               Each inner array consists of two integers, representing the x and y
	 *               coordinates of a point.
	 */
	public RSPolygon(final int[][] points) {
		this(points, 0, 1, 2, 3);
	}

	/**
	 * Constructs an RSPolygon object using a 2D array of coordinates and a set of planes.
	 *
	 * @param points a 2D array where each element represents a vertex of the polygon.
	 *               Each inner array consists of two integers, representing the x and y
	 *               coordinates of a point.
	 * @param planes a variable-length argument array of integers, each representing a plane
	 *               in which the polygon exists. If no planes are specified, the polygon
	 *               will not be associated with any specific planes by default.
	 */
	public RSPolygon(final int[][] points, final int... planes) {
		final int[] xPoints = new int[points.length];
		final int[] yPoints = new int[points.length];
		for (int index = 0; index < points.length; index++) {
			final int[] area = points[index];
			xPoints[index] = area[0];
			yPoints[index] = area[1];
		}
		this.points = points;

		polygon = new EffectiveArea(xPoints, yPoints, points.length);

		this.planes = planes;
		for (int plane : planes) {
			planeSet.set(plane);
		}
	}

	public boolean contains(final int x, final int y) {
		return polygon.contains(x, y);
	}

	public boolean contains(final int x, final int y, final int plane) {
		return containsPlane(plane) && contains(x, y);
	}

	public boolean contains(final Position location) {
		return contains(location.getX(), location.getY(), location.getPlane());
	}

	public boolean containsPlane(int plane) {
		return planeSet.get(plane);
	}

	public ObjectArrayList<Position> getAllPositions(int z, int shrink) {
		var positions = new ObjectArrayList<Position>();
		var west = (int) polygon.getBounds2D().getMinX();
		var east = (int) polygon.getBounds2D().getMaxX();
		var south = (int) polygon.getBounds2D().getMinY();
		var north = (int) polygon.getBounds2D().getMaxY();
		for (int x = west + shrink; x <= east - shrink; x++)
			for (int y = south + shrink; y <= north - shrink; y++)
				positions.add(Position.of(x, y, z));
		return positions;
	}

	public Position getRandomPosition(int z) {
		return getRandomPosition(z, 0);
	}

	public Position getRandomPosition(int z, int shrink) {
		return Random.get(getAllPositions(z, shrink));
	}
}
