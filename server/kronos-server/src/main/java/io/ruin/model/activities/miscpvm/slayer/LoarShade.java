package io.ruin.model.activities.miscpvm.slayer;

import io.ruin.api.utils.Random;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.shared.listeners.HitListener;

public class LoarShade extends NPCCombat {

	private static final int IDLE = 1276;
	private static final int ACTIVE = 1277;


	@Override
	public void init() {
		npc.hitListener = new HitListener().postDamage(this::postDamage);
	}

	private void postDamage(Hit hit) {
		if (npc.getId() != ACTIVE) {
			npc.transform(ACTIVE);
			npc.animate(1288);

		}
	}

	@Override
	public void reset() {
		super.reset();
		if (!isDead() && npc.getId() == ACTIVE) {

			npc.addEvent(event -> {
				event.delay(1);
				npc.transform(IDLE);
			});
		}
	}

	@Override
	public void follow() {
		follow(6);
	}

	@Override
	public boolean attack() {
		if (!withinDistance(6))
			return false;
		if (withinDistance(1) && Random.rollDie(2, 1))
			basicAttack();

		return true;
	}

	@Override
	public void process() {

	}
}
