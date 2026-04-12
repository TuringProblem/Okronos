package io.ruin.model.activities.tempoross;

import io.ruin.model.entity.npc.NPC;
import io.ruin.model.map.object.GameObject;

//Gfx - 1837 for shooting the fish


public class Cannon {

	private GameObject object;
	private NPC pirate;

	private int fishOffsetX, fishOffsetY;

	public Cannon(GameObject object, NPC pirate, int fishOffsetX, int fishOffsetY) {
		this.object = object;
		this.pirate = pirate;
		this.fishOffsetX = fishOffsetX;
		this.fishOffsetY = fishOffsetY;
	}

	public GameObject getObject() {
		return object;
	}

	public NPC getPirate() {
		return pirate;
	}

	public int getPirateState() {
		return pirate.getId() == Tempoross.PIRATE ? 1 : 0;
	}

	public boolean isPirateAlive() {
		return pirate.getId() == Tempoross.PIRATE;
	}

	public int getFishOffsetX() {
		return fishOffsetX;
	}

	public int getFishOffsetY() {
		return fishOffsetY;
	}


}
