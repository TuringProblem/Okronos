package io.ruin.model;

import io.ruin.model.entity.npc.NPC;
import io.ruin.model.map.object.GameObject;

import java.util.Arrays;
import java.util.List;

public class EntityInteractionMode {
	private static final List<Integer> AP_OBJECTS;
	private static final List<Integer> AP_NPCS;

	static { //Add NPCs and objects where you want to skip reach checks such as bankers/Exchange clerks
		AP_NPCS = Arrays.asList(10868, 2898, 6522, 6517, 2897, 2148, 2149, 6528, 2151, 2150, 5422, 3101, 6560, 7317, 1028, 1027, 7663, 19995, 8063, 9855);
		AP_OBJECTS = Arrays.asList(4551, 4550, 14832, 14936, 14937, 14938, 14939, 14944, 14945, 39570, 32738, 34918);
	}

	public static boolean isAPInteraction(NPC npc) {
		if (npc == null)
			return false;

		return AP_NPCS.contains(npc.getId());
	}

	public static boolean isAPObject(GameObject object) {
		if (object == null)
			return false;

		return AP_OBJECTS.contains(object.getId());
	}
}
