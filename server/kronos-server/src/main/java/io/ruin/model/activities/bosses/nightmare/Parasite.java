package io.ruin.model.activities.bosses.nightmare;

import io.ruin.model.entity.npc.NPC;

public class Parasite extends NPC {

	public Parasite(int id) {
		super(id);
	}

	@Override
	public boolean isMovementBlocked(boolean message, boolean ignoreFreeze) {
		return true;
	}

	@Override
	public void process() {
		super.process();
		if (getCombat() != null && getCombat().getTarget() != null && !getCombat().hasAttackDelay()) {
			getCombat().attack();
		}
	}

}
