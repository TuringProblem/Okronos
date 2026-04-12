package core.api;

import static core.api.API.*;
import java.util.ArrayList;
import java.util.List;

// Purpose of having everything within one class is simple:
// import static core.api.APIUtils.*;
//
// Each class here is considered temporary extension of 'core', meaning
// everything here is supposed to be moved to the scripting context as its internal
// api, unless proven slow.
public class APIUtils {

	public static class Coords {
		private final List<Coord> coords = new ArrayList<>();

		public Coords rect(Coord nw, Coord se) {
			var w = se.x() - nw.x();
			var h = nw.y() - se.y();
			for (int dx = 0; dx <= w; dx++) {
				for (int dy = 0; dy < h; dy++) {
					this.coords.add(coord(nw.x() + dx, se.y() + dy));
				}
			}
			return this;
		}

		public Coords around(Coord center, int radius) {
			return rect(center.translate(-radius, radius + 1), center.translate(radius, -radius));
		}

		public Coord random() {
			return rng(coords);
		}
	}

	public static Coords coords() {
		return new Coords();
	}
}
