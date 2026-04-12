package io.ruin.model.map.polly;

import lombok.extern.slf4j.Slf4j;

import java.awt.geom.Rectangle2D;

@Slf4j
public abstract class Curve {
	public abstract int getOrder();

	public abstract double getXTop();
	public abstract double getYTop();
	public abstract double getXBot();
	public abstract double getYBot();

	public abstract double getXMin();
	public abstract double getXMax();

	public abstract double getX0();
	public abstract double getY0();
	public abstract double getX1();
	public abstract double getY1();

	public abstract double XforY(double y);
	public abstract double TforY(double y);
	public abstract double XforT(double t);
	public abstract double YforT(double t);
	public abstract double dXforT(double t, int deriv);
	public abstract double dYforT(double t, int deriv);

	public abstract double nextVertical(double t0, double t1);
	public abstract void enlarge(Rectangle2D r);

	public abstract int getSegment(double[] coords);
	public abstract Curve getReversedCurve();
	public abstract Curve getSubCurve(double ystart, double yend, int dir);

    public static final int INCREASING = 1;
    public static final int DECREASING = -1;

    protected int direction;

    public static void insertQuad(Vector<Curve> curves, double x0, double y0, double[] coords) {
        double y1 = coords[3];
        if (y0 > y1)
            Order2.insert(curves, coords, coords[2], y1, coords[0], coords[1], x0, y0, DECREASING);
		else if (y0 == y1 && y0 == coords[1])
            return;
		else
            Order2.insert(curves, coords, x0, y0, coords[0], coords[1], coords[2], y1, INCREASING);
    }

    public static void insertCubic(Vector<Curve> curves, double x0, double y0, double[] coords) {
        double y1 = coords[5];
        if (y0 > y1)
            Order3.insert(curves, coords, coords[4], y1, coords[2], coords[3], coords[0], coords[1], x0, y0, DECREASING);
        else if (y0 == y1 && y0 == coords[1] && y0 == coords[3])
            return;
        else
            Order3.insert(curves, coords, x0, y0, coords[0], coords[1], coords[2], coords[3], coords[4], y1, INCREASING);
    }

    public Curve(int direction) {
        this.direction = direction;
    }

    public final int getDirection() {
        return direction;
    }

    public final Curve getWithDirection(int direction) {
        return (this.direction == direction ? this : getReversedCurve());
    }

    public static double round(double v) {
        return v;
    }

    public static int orderof(double x1, double x2) {
		return Double.compare(x1, x2);
	}

    public static long signeddiffbits(double y1, double y2) {
        return (Double.doubleToLongBits(y1) - Double.doubleToLongBits(y2));
    }
    public static long diffbits(double y1, double y2) {
        return Math.abs(Double.doubleToLongBits(y1) - Double.doubleToLongBits(y2));
    }

    public static double prev(double v) {
        return Double.longBitsToDouble(Double.doubleToLongBits(v)-1);
    }

    public static double next(double v) {
        return Double.longBitsToDouble(Double.doubleToLongBits(v)+1);
    }

    public int crossingsFor(double x, double y) {
        if (y >= getYTop() && y < getYBot()) if (x < getXMax() && (x < getXMin() || x < XforY(y))) return 1;
        return 0;
    }

    public boolean accumulateCrossings(Crossings c) {
        double xhi = c.getXHi();
        if (getXMin() >= xhi) return false;
        double xlo = c.getXLo();
        double ylo = c.getYLo();
        double yhi = c.getYHi();
        double y0 = getYTop();
        double y1 = getYBot();
        double tstart, ystart, tend, yend;
        if (y0 < ylo) {
            if (y1 <= ylo) return false;
            ystart = ylo;
            tstart = TforY(ylo);
        } else {
            if (y0 >= yhi) return false;
            ystart = y0;
            tstart = 0;
        }
        if (y1 > yhi) {
            yend = yhi;
            tend = TforY(yhi);
        } else {
            yend = y1;
            tend = 1;
        }
        boolean hitLo = false;
        boolean hitHi = false;
        while (true) {
            double x = XforT(tstart);
            if (x < xhi) {
                if (hitHi || x > xlo) return true;
                hitLo = true;
            } else {
                if (hitLo) return true;
                hitHi = true;
            }
            if (tstart >= tend) break;
            tstart = nextVertical(tstart, tend);
        }
        if (hitLo) c.record(ystart, yend, direction);
        return false;
    }

    public Curve getSubCurve(double ystart, double yend) {
        return getSubCurve(ystart, yend, direction);
    }

    public int compareTo(Curve that, double[] yrange) {
        double y0 = yrange[0];
        double y1 = yrange[1];
        y1 = Math.min(Math.min(y1, this.getYBot()), that.getYBot());
        if (y1 <= yrange[0]) {
			log.error("this == {}", this);
			log.error("that == {}", that);
			log.error("target range = {}=>{}", yrange[0], yrange[1]);
            throw new InternalError("backstepping from "+yrange[0]+" to "+y1);
        }
        yrange[1] = y1;
        if (this.getXMax() <= that.getXMin()) {
            if (this.getXMin() == that.getXMax()) return 0;
            return -1;
        }
        if (this.getXMin() >= that.getXMax()) return 1;
        double s0 = this.TforY(y0);
        double ys0 = this.YforT(s0);
        if (ys0 < y0) {
            s0 = refineTforY(s0, ys0, y0);
            ys0 = this.YforT(s0);
        }
        double s1 = this.TforY(y1);
		if (this.YforT(s1) < y0) s1 = refineTforY(s1, this.YforT(s1), y0);
        double t0 = that.TforY(y0);
        double yt0 = that.YforT(t0);
        if (yt0 < y0) {
            t0 = that.refineTforY(t0, yt0, y0);
            yt0 = that.YforT(t0);
        }
        double t1 = that.TforY(y1);
		if (that.YforT(t1) < y0) t1 = that.refineTforY(t1, that.YforT(t1), y0);
        double xs0 = this.XforT(s0);
        double xt0 = that.XforT(t0);
        double scale = Math.max(Math.abs(y0), Math.abs(y1));
        double ymin = Math.max(scale * 1E-14, 1E-300);
        if (fairlyClose(xs0, xt0)) {
            double bump = ymin;
            double maxbump = Math.min(ymin * 1E13, (y1 - y0) * .1);
            double y = y0 + bump;
            while (y <= y1) {
                if (fairlyClose(this.XforY(y), that.XforY(y))) {
                    if ((bump *= 2) > maxbump) bump = maxbump;
                }
				else {
                    y -= bump;
                    while (true) {
                        bump /= 2;
                        double newy = y + bump;
                        if (newy <= y) break;
                        if (fairlyClose(this.XforY(newy), that.XforY(newy))) y = newy;
                    }
                    break;
                }
                y += bump;
            }
            if (y > y0) {
                if (y < y1) yrange[1] = y;
                return 0;
            }
        }
        if (ymin <= 0) System.out.println("ymin = " + ymin);
        while (s0 < s1 && t0 < t1) {
            double sh = this.nextVertical(s0, s1);
            double xsh = this.XforT(sh);
            double ysh = this.YforT(sh);
            double th = that.nextVertical(t0, t1);
            double xth = that.XforT(th);
            double yth = that.YforT(th);
        try {
            if (findIntersect(that, yrange, ymin, 0, 0,
                              s0, xs0, ys0, sh, xsh, ysh,
                              t0, xt0, yt0, th, xth, yth)) break;
        }
		catch (Throwable t) {
			log.error("Error: {}", String.valueOf(t));
			log.error("y range was {}=>{}", yrange[0], yrange[1]);
			log.error("s y range is {}=>{}", ys0, ysh);
			log.error("t y range is {}=>{}", yt0, yth);
			log.error("ymin is {}", ymin);
            return 0;
        }
            if (ysh < yth) {
                if (ysh > yrange[0]) {
                    if (ysh < yrange[1]) yrange[1] = ysh;
                    break;
                }
                s0 = sh;
                xs0 = xsh;
                ys0 = ysh;
            }
			else {
                if (yth > yrange[0]) {
                    if (yth < yrange[1]) yrange[1] = yth;
                    break;
                }
                t0 = th;
                xt0 = xth;
                yt0 = yth;
            }
        }
        double ymid = (yrange[0] + yrange[1]) / 2;
        return orderof(this.XforY(ymid), that.XforY(ymid));
    }

    public static final double TMIN = 1E-3;

    public boolean findIntersect(Curve that, double[] yrange, double ymin,
								 int slevel, int tlevel,
								 double s0, double xs0, double ys0,
								 double s1, double xs1, double ys1,
								 double t0, double xt0, double yt0,
								 double t1, double xt1, double yt1) {
        if (ys0 > yt1 || yt0 > ys1) return false;
        if (Math.min(xs0, xs1) > Math.max(xt0, xt1) ||
            Math.max(xs0, xs1) < Math.min(xt0, xt1))
			return false;
        if (s1 - s0 > TMIN) {
            double s = (s0 + s1) / 2;
            double xs = this.XforT(s);
            double ys = this.YforT(s);
            if (s == s0 || s == s1) {
                System.out.println("s0 = "+s0);
                System.out.println("s1 = "+s1);
                throw new InternalError("no s progress!");
            }
            if (t1 - t0 > TMIN) {
                double t = (t0 + t1) / 2;
                double xt = that.XforT(t);
                double yt = that.YforT(t);
                if (t == t0 || t == t1) {
					log.error("t0 = {}", t0);
					log.error("t1 = {}", t1);
                    throw new InternalError("no t progress!");
                }
                if (ys >= yt0 && yt >= ys0) if (findIntersect(that, yrange, ymin, slevel + 1, tlevel + 1,
					s0, xs0, ys0, s, xs, ys,
					t0, xt0, yt0, t, xt, yt)) return true;
                if (ys >= yt) if (findIntersect(that, yrange, ymin, slevel + 1, tlevel + 1,
					s0, xs0, ys0, s, xs, ys,
					t, xt, yt, t1, xt1, yt1)) return true;
                if (yt >= ys) if (findIntersect(that, yrange, ymin, slevel + 1, tlevel + 1,
					s, xs, ys, s1, xs1, ys1,
					t0, xt0, yt0, t, xt, yt)) return true;
                if (ys1 >= yt && yt1 >= ys) return findIntersect(that, yrange, ymin, slevel + 1, tlevel + 1,
					s, xs, ys, s1, xs1, ys1,
					t, xt, yt, t1, xt1, yt1);
            }
			else {
                if (ys >= yt0) if (findIntersect(that, yrange, ymin, slevel + 1, tlevel,
					s0, xs0, ys0, s, xs, ys,
					t0, xt0, yt0, t1, xt1, yt1)) return true;
                if (yt1 >= ys) return findIntersect(that, yrange, ymin, slevel + 1, tlevel,
					s, xs, ys, s1, xs1, ys1,
					t0, xt0, yt0, t1, xt1, yt1);
            }
        }
		else if (t1 - t0 > TMIN) {
            double t = (t0 + t1) / 2;
            double xt = that.XforT(t);
            double yt = that.YforT(t);
            if (t == t0 || t == t1) {
				log.error("t0 = {}", t0);
				log.error("t1 = {}", t1);
                throw new InternalError("no t progress!");
            }
            if (yt >= ys0) if (findIntersect(that, yrange, ymin, slevel, tlevel + 1,
				s0, xs0, ys0, s1, xs1, ys1,
				t0, xt0, yt0, t, xt, yt)) return true;
            if (ys1 >= yt) return findIntersect(that, yrange, ymin, slevel, tlevel + 1,
				s0, xs0, ys0, s1, xs1, ys1,
				t, xt, yt, t1, xt1, yt1);
        }
		else {
            double xlk = xs1 - xs0;
            double ylk = ys1 - ys0;
            double xnm = xt1 - xt0;
            double ynm = yt1 - yt0;
            double xmk = xt0 - xs0;
            double ymk = yt0 - ys0;
            double det = xnm * ylk - ynm * xlk;
            if (det != 0) {
                double detinv = 1 / det;
                double s = (xnm * ymk - ynm * xmk) * detinv;
                double t = (xlk * ymk - ylk * xmk) * detinv;
                if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
                    s = s0 + s * (s1 - s0);
                    t = t0 + t * (t1 - t0);
                    if (s < 0 || s > 1 || t < 0 || t > 1) System.out.println("Uh oh!");
                    double y = (this.YforT(s) + that.YforT(t)) / 2;
                    if (y <= yrange[1] && y > yrange[0]) {
                        yrange[1] = y;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public double refineTforY(double t0, double yt0, double y0) {
        double t1 = 1;
        while (true) {
            double th = (t0 + t1) / 2;
            if (th == t0 || th == t1) return t1;
            double y = YforT(th);
            if (y < y0) {
                t0 = th;
                yt0 = y;
            } else if (y > y0) t1 = th;
			else return t1;
        }
    }

    public boolean fairlyClose(double v1, double v2) {
        return (Math.abs(v1 - v2) < Math.max(Math.abs(v1), Math.abs(v2)) * 1E-10);
    }

}
