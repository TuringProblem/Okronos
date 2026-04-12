package io.ruin.model.map.polly;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-06-06
 */
public final class EvenOdd extends Crossings {
	public EvenOdd(double xlo, double ylo, double xhi, double yhi) {
		super(xlo, ylo, xhi, yhi);
	}

	public boolean covers(double ystart, double yend) {
		return (limit == 2 && yRanges[0] <= ystart && yRanges[1] >= yend);
	}

	public void record(double ystart, double yend, int direction) {
		if (ystart >= yend) return;
		int from = 0;
		while (from < limit && ystart > yRanges[from + 1]) from += 2;
		int to = from;
		while (from < limit) {
			double yrlo = yRanges[from++];
			double yrhi = yRanges[from++];
			if (yend < yrlo) {
				yRanges[to++] = ystart;
				yRanges[to++] = yend;
				ystart = yrlo;
				yend = yrhi;
				continue;
			}
			double yll, ylh, yhl, yhh;
			if (ystart < yrlo) {
				yll = ystart;
				ylh = yrlo;
			}
			else {
				yll = yrlo;
				ylh = ystart;
			}
			if (yend < yrhi) {
				yhl = yend;
				yhh = yrhi;
			}
			else {
				yhl = yrhi;
				yhh = yend;
			}
			if (ylh == yhl) {
				ystart = yll;
				yend = yhh;
			}
			else {
				if (ylh > yhl) {
					ystart = yhl;
					yhl = ylh;
					ylh = ystart;
				}
				if (yll != ylh) {
					yRanges[to++] = yll;
					yRanges[to++] = ylh;
				}
				ystart = yhl;
				yend = yhh;
			}
			if (ystart >= yend) break;
		}
		if (to < from && from < limit) System.arraycopy(yRanges, from, yRanges, to, limit - from);
		to += (limit - from);
		if (ystart < yend) {
			if (to >= yRanges.length) {
				double[] newranges = new double[to + 10];
				System.arraycopy(yRanges, 0, newranges, 0, to);
				yRanges = newranges;
			}
			yRanges[to++] = ystart;
			yRanges[to++] = yend;
		}
		limit = to;
	}
}
