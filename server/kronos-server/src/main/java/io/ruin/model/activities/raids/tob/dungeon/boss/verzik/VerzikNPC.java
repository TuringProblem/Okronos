package io.ruin.model.activities.raids.tob.dungeon.boss.verzik;

import io.ruin.model.activities.raids.tob.dungeon.TheatreBoss;
import io.ruin.model.activities.raids.tob.party.TheatreParty;

public class VerzikNPC extends TheatreBoss {

	public enum Form {
		NORMAL(8372), // First phase
		RANGED(8374) // Second Phase
		;

		Form(int npcId) {
			this.npcId = npcId;
		}

		int npcId;
	}

	private int stage = 0;

	public VerzikNPC(int id, TheatreParty party) {
		super(id, party);
		VerzikCombat verzikCombat = (VerzikCombat) this.getCombat();
//        npc.transform(8374);
	}


	public void setStage(int stage) {
		this.stage = stage;
	}
}
