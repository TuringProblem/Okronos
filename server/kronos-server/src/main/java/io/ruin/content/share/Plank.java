package io.ruin.content.share;

public enum Plank {
	WOOD(1, 1511, 960),
	OAK(3, 1521, 8778),
	TEAK(5, 6333, 8780),
	MAHOGANY(7, 6332, 8782);
	public final int cost, woodId, plankId;

	Plank(int cost, int woodId, int plankId) {
		this.cost = cost;
		this.woodId = woodId;
		this.plankId = plankId;
	}

	public static Plank getFromLog(int logId) {
		for (Plank p : values()) {
			if (p.woodId == logId) {
				return p;
			}
		}
		return null;
	}
}