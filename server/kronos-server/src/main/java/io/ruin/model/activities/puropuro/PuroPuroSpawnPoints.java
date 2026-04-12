package io.ruin.model.activities.puropuro;

import io.ruin.model.map.Position;
import io.ruin.api.utils.Random;

public class PuroPuroSpawnPoints {

	// Public class to hold spawn point data
	public record SpawnPosition(int x, int y, boolean horizontal) {}

	// Public static array of spawn positions
	public static final SpawnPosition[] SPAWNS = {
		new SpawnPosition(2572, 4348, false),
		new SpawnPosition(2606, 4348, false),
		new SpawnPosition(2603, 4346, true),
		new SpawnPosition(2569, 4345, false),
		new SpawnPosition(2576, 4342, false),
		new SpawnPosition(2580, 4343, true),
		new SpawnPosition(2606, 4342, false),
		new SpawnPosition(2601, 4340, true),
		new SpawnPosition(2595, 4343, true),
		new SpawnPosition(2607, 4339, false),
		// Add all your spawn positions here...
		new SpawnPosition(2607, 4301, false)
	};

	// Helper method to get random spawn position
	public static Position getRandomSpawnPosition(int convertedX, int convertedY) {
		SpawnPosition spawn = SPAWNS[Random.get(SPAWNS.length - 1)];
		return Position.of(convertedX + spawn.x, convertedY + spawn.y, 0);
	}
}