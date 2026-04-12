package io.ruin.model.entity.npc;

import java.util.List;

import io.ruin.model.map.Position;
import io.ruin.model.map.Region;
import org.rsmod.util.EntityHelperKt;

// Functions for all available npcs
public class NPCS {

	public static List<NPC> inZone(Position position) {
		return inZone(position, 1);
	}

	public static List<NPC> inZone(Position position, int radius) {
		return EntityHelperKt.searchZoneNpcs(position.x(), position.y(), position.plane(), radius);
	}
}
