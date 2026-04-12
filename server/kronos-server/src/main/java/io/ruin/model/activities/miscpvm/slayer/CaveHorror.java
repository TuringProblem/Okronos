package io.ruin.model.activities.miscpvm.slayer;

import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.item.containers.Equipment;

public class CaveHorror extends NPCCombat {

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
		basicAttack();
		return true;
	}

	@Override
	public void process() {

	}
}
