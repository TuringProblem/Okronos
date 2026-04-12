package io.ruin.model.map.polly;

import java.awt.geom.PathIterator;

public abstract class Crossings {
	public static final boolean debug = false;

	int limit = 0;
	double[] yRanges = new double[10];

	double xlo, ylo, xhi, yhi;

	public Crossings(double xlo, double ylo, double xhi, double yhi) {
		this.xlo = xlo;
		this.ylo = ylo;
		this.xhi = xhi;
		this.yhi = yhi;
	}

	public final double getXLo() {
		return xlo;
	}

	public final double getYLo() {
		return ylo;
	}

	public final double getXHi() {
		return xhi;
	}

	public final double getYHi() {
		return yhi;
	}

	public abstract void record(double ystart, double yend, int direction);

	public final boolean isEmpty() {
		return (limit == 0);
	}

	public abstract boolean covers(double ystart, double yend);

	public static Crossings findCrossings(Vector<? extends Curve> curves, double xlo, double ylo, double xhi, double yhi) {
		var cross = new EvenOdd(xlo, ylo, xhi, yhi);
		var enum_ = curves.elements();
		while (enum_.hasMoreElements()) {
			var c = enum_.nextElement();
			if (c.accumulateCrossings(cross))
				return null;
		}
		return cross;
	}

	public static Crossings findCrossings(PathIterator pi, double xlo, double ylo, double xhi, double yhi) {
		Crossings cross;
		if (pi.getWindingRule() == PathIterator.WIND_EVEN_ODD)
			cross = new EvenOdd(xlo, ylo, xhi, yhi);
		else
			cross = new NonZero(xlo, ylo, xhi, yhi);

		var coords = new double[23];
		var movx = 0.0D;
		var movy = 0.0D;
		var curx = 0.0D;
		var cury = 0.0D;
		var newx = 0.0D;
		var newy = 0.0D;
		while (!pi.isDone()) {
			int type = pi.currentSegment(coords);
			switch (type) {
				case PathIterator.SEG_MOVETO:
					if (movy != cury && cross.accumulateLine(curx, cury, movx, movy))
						return null;

					movx = curx = coords[0];
					movy = cury = coords[1];
					break;
				case PathIterator.SEG_LINETO:
					newx = coords[0];
					newy = coords[1];
					if (cross.accumulateLine(curx, cury, newx, newy))
						return null;

					curx = newx;
					cury = newy;
					break;
				case PathIterator.SEG_QUADTO:
					newx = coords[2];
					newy = coords[3];
					if (cross.accumulateQuad(curx, cury, coords))
						return null;

					curx = newx;
					cury = newy;
					break;
				case PathIterator.SEG_CUBICTO:
					newx = coords[4];
					newy = coords[5];
					if (cross.accumulateCubic(curx, cury, coords))
						return null;

					curx = newx;
					cury = newy;
					break;
				case PathIterator.SEG_CLOSE:
					if (movy != cury && cross.accumulateLine(curx, cury, movx, movy))
						return null;

					curx = movx;
					cury = movy;
					break;
			}
			pi.next();
		}
		if (movy != cury)
			if (cross.accumulateLine(curx, cury, movx, movy))
				return null;
		return cross;
	}

	public boolean accumulateLine(double x0, double y0, double x1, double y1) {
		if (y0 <= y1)
			return accumulateLine(x0, y0, x1, y1, 1);
		else
			return accumulateLine(x1, y1, x0, y0, -1);
	}

	public boolean accumulateLine(double x0, double y0, double x1, double y1, int direction) {
		if (yhi <= y0 || ylo >= y1) return false;
		if (x0 >= xhi && x1 >= xhi) return false;
		if (y0 == y1) return (x0 >= xlo || x1 >= xlo);
		double xstart, ystart, xend, yend;
		double dx = (x1 - x0);
		double dy = (y1 - y0);
		if (y0 < ylo) {
			xstart = x0 + (ylo - y0) * dx / dy;
			ystart = ylo;
		}
		else {
			xstart = x0;
			ystart = y0;
		}
		if (yhi < y1) {
			xend = x0 + (yhi - y0) * dx / dy;
			yend = yhi;
		}
		else {
			xend = x1;
			yend = y1;
		}
		if (xstart >= xhi && xend >= xhi) return false;
		if (xstart > xlo || xend > xlo) return true;
		record(ystart, yend, direction);
		return false;
	}

	private final Vector<Curve> tmp = new Vector<>();

	public boolean accumulateQuad(double x0, double y0, double[] coords) {
		if (y0 < ylo && coords[1] < ylo && coords[3] < ylo) return false;
		if (y0 > yhi && coords[1] > yhi && coords[3] > yhi) return false;
		if (x0 > xhi && coords[0] > xhi && coords[2] > xhi) return false;
		if (x0 < xlo && coords[0] < xlo && coords[2] < xlo) {
			if (y0 < coords[3]) record(Math.max(y0, ylo), Math.min(coords[3], yhi), 1);
			else if (y0 > coords[3]) record(Math.max(coords[3], ylo), Math.min(y0, yhi), -1);
			return false;
		}
		Curve.insertQuad(tmp, x0, y0, coords);
		var enum_ = tmp.elements();
		while (enum_.hasMoreElements()) {
			var c = enum_.nextElement();
			if (c.accumulateCrossings(this)) return true;
		}
		tmp.clear();
		return false;
	}

	public boolean accumulateCubic(double x0, double y0, double[] coords) {
		if (y0 < ylo && coords[1] < ylo &&
			coords[3] < ylo && coords[5] < ylo) return false;
		if (y0 > yhi && coords[1] > yhi &&
			coords[3] > yhi && coords[5] > yhi) return false;
		if (x0 > xhi && coords[0] > xhi &&
			coords[2] > xhi && coords[4] > xhi) return false;
		if (x0 < xlo && coords[0] < xlo &&
			coords[2] < xlo && coords[4] < xlo) {
			if (y0 <= coords[5]) record(Math.max(y0, ylo), Math.min(coords[5], yhi), 1);
			else record(Math.max(coords[5], ylo), Math.min(y0, yhi), -1);
			return false;
		}
		Curve.insertCubic(tmp, x0, y0, coords);
		var enum_ = tmp.elements();
		while (enum_.hasMoreElements()) {
			var c = enum_.nextElement();
			if (c.accumulateCrossings(this)) return true;
		}
		tmp.clear();
		return false;
	}

}
