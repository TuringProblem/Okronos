package io.ruin.model.activities.deadman;

public class LeveledChunk {

	public int chunkId, level;

	public LeveledChunk(int id) {
		this.chunkId = id;
	}

	public LeveledChunk(int id, int level) {
		this.chunkId = id;
		this.level = level;
	}
}