package gemstonecrab;

import io.ruin.model.entity.npc.NPC;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.MultiZone;
import io.ruin.model.map.Position;

public class Init {
	public static void init() {
		MultiZone.add(new Bounds(1340, 3091, 1366, 3130, 0));
		NPC gemstoneCrab = new NPC(14779).spawn(new Position(1351, 3110, 0), Direction.SOUTH);
	}
}
