package io.ruin.model.activities.raids.toa.bosses.kephri;

import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.shared.StepType;

public class Swarm extends NPC {
	public Swarm(int id) {
		super(id);
	}

	public void moveTowardsKephri(NPC kephri) {
		int x = kephri.getAbsX() - this.npc.getAbsX();
		int y = kephri.getAbsY() - this.npc.getAbsY();
		this.npc.step(x, y, StepType.WALK);
	}
}
