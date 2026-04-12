package io.ruin.model.activities.raids.tob.dungeon.boss.vasilias;

import io.ruin.model.activities.raids.tob.dungeon.TheatreBoss;
import io.ruin.model.activities.raids.tob.party.TheatreParty;

public class VasiliasNPC extends TheatreBoss {
	private int stage = 0;

	/**
	 * Creates a new theatre boss object.
	 *
	 * @param id    the npc id.
	 * @param party
	 */
	public VasiliasNPC(int id, TheatreParty party) {
		super(id, party);
		VasiliasCombat vasiliasCombat = (VasiliasCombat) this.getCombat();
	}

	public void setStage(int stage) {
		this.stage = stage;
	}
}
