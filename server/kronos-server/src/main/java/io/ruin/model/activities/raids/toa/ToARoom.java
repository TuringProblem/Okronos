package io.ruin.model.activities.raids.toa;

import io.ruin.model.entity.npc.NPC;
import io.ruin.model.map.Position;
import io.ruin.model.map.dynamic.DynamicMap;

import java.util.ArrayList;
import java.util.List;

public abstract class ToARoom {
	public DynamicMap map;
	public boolean roomCompleted = false;

	public List<NPC> npcs = new ArrayList<>();

	public abstract void buildRoom() throws DynamicMap.DynamicMapBuildException;

	public abstract void populateRoom();

	public abstract Position getEnterPosition();
}
