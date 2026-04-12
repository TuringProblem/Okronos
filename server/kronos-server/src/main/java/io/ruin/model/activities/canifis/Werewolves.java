package io.ruin.model.activities.canifis;

import io.ruin.api.utils.Random;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;

public class Werewolves extends NPCCombat {

	private boolean transform;

	@Override
	public void init() {

	}

	@Override
	public void follow() {
		follow(1);
	}

	@Override
	public boolean attack() {
		if (!withinDistance(1))
			return false;
		if (npc.getMaxHp() / 1.25 >= npc.getHp() && !transform) {
			npc.transform(2593 + Random.get(17));
			transform = true;
			basicAttack();
			return true;
		}
		basicAttack();
		return true;
	}

	@Override
	public void process() {

	}

}