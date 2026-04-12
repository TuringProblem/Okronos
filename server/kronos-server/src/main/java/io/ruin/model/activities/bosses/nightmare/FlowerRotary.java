package io.ruin.model.activities.bosses.nightmare;

import io.ruin.model.map.Position;

public enum FlowerRotary {

	ALPHA(new int[]{1, 1}, new int[]{-1, -1}),
	BETA(new int[]{1, -1}, new int[]{-1, 1}),
	GAMMA(new int[]{-1, 1}, new int[]{1, -1}),
	DELTA(new int[]{-1, 1}, new int[]{1, -1});

	private int[] light, dark;

	private FlowerRotary(int[] light, int[] dark) {
		this.light = light;
		this.dark = dark;
	}

	public int[] getLight() {
		return light;
	}

	public int[] getDark() {
		return dark;
	}

	public boolean safe(Position center, Position location) {

		Position n = center.translated(getLight()[0] * 10, getLight()[1] * 10, 0);
		if (n.getX() > center.getX()) {
			if (location.getX() < center.getX()) {
				return false;
			}
		} else {
			if (location.getX() > center.getX()) {
				return false;
			}
		}
		if (n.getY() > center.getY()) {
			if (location.getY() < center.getY()) {
				return false;
			}
		} else {
			if (location.getY() > center.getY()) {
				return false;
			}
		}
		return true;

	}
}
