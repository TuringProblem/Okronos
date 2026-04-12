package io.ruin.model.activities.raids.tob.dungeon.boss.sotetseg;

import io.ruin.model.activities.raids.tob.dungeon.TheatreBoss;
import io.ruin.model.activities.raids.tob.party.TheatreParty;

public class SotetsegNPC extends TheatreBoss {

	private int stage = 0;

	public SotetsegNPC(int id, TheatreParty party) {
		super(id, party);
		SotetsegCombat sotetsegCombat = (SotetsegCombat) this.getCombat();
		npc.transform(8388);
	}


	public void setStage(int stage) {
		this.stage = stage;
	}
}
