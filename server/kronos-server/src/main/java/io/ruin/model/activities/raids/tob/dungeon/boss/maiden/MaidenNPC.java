package io.ruin.model.activities.raids.tob.dungeon.boss.maiden;

import io.ruin.model.activities.raids.tob.dungeon.TheatreBoss;
import io.ruin.model.activities.raids.tob.party.TheatreParty;


public class MaidenNPC extends TheatreBoss {

	private int stage = 0;

	public MaidenNPC(int id, TheatreParty party) {
		super(id, party);

		MaidenCombat maidenCombat = (MaidenCombat) this.getCombat();

	}

	public void setStage(int stage) {
		this.stage = stage;
	}
}
