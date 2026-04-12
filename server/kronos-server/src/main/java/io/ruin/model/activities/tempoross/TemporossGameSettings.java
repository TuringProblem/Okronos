package io.ruin.model.activities.tempoross;

import io.ruin.model.map.Position;

public enum TemporossGameSettings {

	NOVICE(7, new Position(3139, 2841, 0));


	private final int points;
	private final Position landerExit;


	TemporossGameSettings(int points, Position landerExit) {
		this.points = points;
		this.landerExit = landerExit;
	}

	public int points() {
		return points;
	}

	public Position exitTile() {
		return landerExit;
	}

}
