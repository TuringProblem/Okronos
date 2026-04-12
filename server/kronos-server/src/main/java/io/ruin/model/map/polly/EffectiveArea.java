package io.ruin.model.map.polly;

import java.awt.*;
import java.util.Arrays;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-06-06
 */
public class EffectiveArea extends Polygon {

	EffectiveArea(int[] xpoints, int[] ypoints, int npoints) {
		if (npoints > xpoints.length || npoints > ypoints.length)
			throw new IndexOutOfBoundsException("npoints > xpoints.length || npoints > ypoints.length");
		if (npoints < 0)
			throw new NegativeArraySizeException("npoints < 0");

		this.npoints = npoints;
		this.xpoints = Arrays.copyOf(xpoints, npoints);
		this.ypoints = Arrays.copyOf(ypoints, npoints);
		this.boundingBox = getBounds();
	}

	private final Rectangle boundingBox;

	@Override
	@Deprecated
	public void addPoint(int x, int y) {
		throw new IllegalStateException();
	}

	@Override
	public boolean contains(double x, double y) {
		if (npoints <= 2 || !boundingBox.contains(x, y)) return false;
		int hits = 0;

		int lastx = xpoints[npoints - 1];
		int lasty = ypoints[npoints - 1];
		int curx, cury;

		// Walk the edges of the polygon
		for (int i = 0; i < npoints; lastx = curx, lasty = cury, i++) {
			curx = xpoints[i];
			cury = ypoints[i];
			if (cury == lasty) continue;

			int leftx;
			if (curx < lastx) {
				if (x >= lastx) continue;
				leftx = curx;
			}
			else {
				if (x >= curx) continue;
				leftx = lastx;
			}
			double test1, test2;
			if (cury < lasty) {
				if (y < cury || y >= lasty) continue;
				if (x < leftx) {
					hits++;
					continue;
				}
				test1 = x - curx;
				test2 = y - cury;
			}
			else {
				if (y < lasty || y >= cury) continue;
				if (x < leftx) {
					hits++;
					continue;
				}
				test1 = x - lastx;
				test2 = y - lasty;
			}
			if (test1 < (test2 / (lasty - cury) * (lastx - curx))) hits++;
		}
		return ((hits & 1) != 0);
	}
}

