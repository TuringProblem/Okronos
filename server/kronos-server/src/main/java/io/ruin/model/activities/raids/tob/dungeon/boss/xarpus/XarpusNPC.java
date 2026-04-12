package io.ruin.model.activities.raids.tob.dungeon.boss.xarpus;

import io.ruin.model.activities.raids.tob.dungeon.TheatreBoss;
import io.ruin.model.activities.raids.tob.party.TheatreParty;

public class XarpusNPC extends TheatreBoss {

	private int stage = 0;

	public XarpusNPC(int id, TheatreParty party) {
		super(id, party);
		XarpusCombat xarpusCombat = (XarpusCombat) this.getCombat();
//        npc.transform(8340);
	}


	public void setStage(int stage) {
		this.stage = stage;
	}
}
