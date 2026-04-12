package io.ruin.model.activities.raids.tob.dungeon.boss.bloat;

import io.ruin.model.activities.raids.tob.dungeon.TheatreBoss;
import io.ruin.model.activities.raids.tob.dungeon.boss.xarpus.XarpusCombat;
import io.ruin.model.activities.raids.tob.party.TheatreParty;

public class BloatNPC extends TheatreBoss {

	private int stage = 0;

	public BloatNPC(int id, TheatreParty party) {
		super(id, party);
		BloatCombat bloatCombat = (BloatCombat) this.getCombat();
	}


	public void setStage(int stage) {
		this.stage = stage;
	}
}