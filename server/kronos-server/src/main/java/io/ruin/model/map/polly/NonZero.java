package io.ruin.model.map.polly;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-06-06
 */
public final class NonZero extends Crossings {
	private int[] crossCounts;

	public NonZero(double xlo, double ylo, double xhi, double yhi) {
		super(xlo, ylo, xhi, yhi);
		crossCounts = new int[yRanges.length / 2];
	}

	public boolean covers(double ystart, double yend) {
		int i = 0;
		while (i < limit) {
			double ylo = yRanges[i++];
			double yhi = yRanges[i++];
			if (ystart >= yhi) continue;
			if (ystart < ylo) return false;
			if (yend <= yhi) return true;
			ystart = yhi;
		}
		return (ystart >= yend);
	}

	public void remove(int cur) {
		limit -= 2;
		int rem = limit - cur;
		if (rem > 0) {
			System.arraycopy(yRanges, cur + 2, yRanges, cur, rem);
			System.arraycopy(crossCounts, cur / 2 + 1,
				crossCounts, cur / 2,
				rem / 2);
		}
	}

	public void insert(int cur, double lo, double hi, int dir) {
		int rem = limit - cur;
		double[] oldranges = yRanges;
		int[] oldcounts = crossCounts;
		if (limit >= yRanges.length) {
			yRanges = new double[limit + 10];
			System.arraycopy(oldranges, 0, yRanges, 0, cur);
			crossCounts = new int[(limit + 10) / 2];
			System.arraycopy(oldcounts, 0, crossCounts, 0, cur / 2);
		}
		if (rem > 0) {
			System.arraycopy(oldranges, cur, yRanges, cur + 2, rem);
			System.arraycopy(oldcounts, cur / 2,
				crossCounts, cur / 2 + 1,
				rem / 2);
		}
		yRanges[cur + 0] = lo;
		yRanges[cur + 1] = hi;
		crossCounts[cur / 2] = dir;
		limit += 2;
	}

	public void record(double ystart, double yend, int direction) {
		if (ystart >= yend) return;
		int cur = 0;
		while (cur < limit && ystart > yRanges[cur + 1]) cur += 2;
		if (cur < limit) {
			int rdir = crossCounts[cur / 2];
			double yrlo = yRanges[cur + 0];
			double yrhi = yRanges[cur + 1];
			if (yrhi == ystart && rdir == direction) {
				if (cur + 2 == limit) {
					yRanges[cur + 1] = yend;
					return;
				}
				remove(cur);
				ystart = yrlo;
				rdir = crossCounts[cur / 2];
				yrlo = yRanges[cur + 0];
				yrhi = yRanges[cur + 1];
			}
			if (yend < yrlo) {
				insert(cur, ystart, yend, direction);
				return;
			}
			if (yend == yrlo && rdir == direction) {
				yRanges[cur] = ystart;
				return;
			}
			if (ystart < yrlo) {
				insert(cur, ystart, yrlo, direction);
				cur += 2;
				ystart = yrlo;
			}
			else if (yrlo < ystart) {
				insert(cur, yrlo, ystart, rdir);
				cur += 2;
				yrlo = ystart;
			}
			int newdir = rdir + direction;
			double newend = Math.min(yend, yrhi);
			if (newdir == 0) remove(cur);
			else {
				crossCounts[cur / 2] = newdir;
				yRanges[cur++] = ystart;
				yRanges[cur++] = newend;
			}
			ystart = yrlo = newend;
			if (yrlo < yrhi) insert(cur, yrlo, yrhi, rdir);
		}
		if (ystart < yend) insert(cur, ystart, yend, direction);
	}
}
