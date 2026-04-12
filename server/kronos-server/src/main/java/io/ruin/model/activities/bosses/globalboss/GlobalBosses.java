package io.ruin.model.activities.bosses.globalboss;

import io.ruin.model.entity.npc.NPC;
import io.ruin.model.map.Position;

public enum GlobalBosses {
	// YOWLING_YETI(new NPC(853), new Position(2641, 3612, 0)),
	STONE_POLTEGEIST(new NPC(11896), new Position(2912, 3614, 0)),
	BLOOD_REAPER(new NPC(11895), new Position(2912, 3614, 0));

	NPC boss;
	Position spawnPosition;

	GlobalBosses(NPC boss, Position spawnPosition) {
		this.boss = boss;
		this.spawnPosition = spawnPosition;
	}
}
