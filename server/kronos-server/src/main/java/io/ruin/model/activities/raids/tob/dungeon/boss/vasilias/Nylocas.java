package io.ruin.model.activities.raids.tob.dungeon.boss.vasilias;

import io.ruin.model.activities.miscpvm.BasicCombat;
import io.ruin.model.entity.Entity;
import io.ruin.utility.Misc;

//Pillar attackers (32)
public class Nylocas extends BasicCombat {

	@Override
	public boolean attack() {
		if (target.npc != null) {
			int centerX = target.getAbsX() + 1;
			int centerY = target.getAbsY() + 1;
			int distance = Misc.getDistance(npc.getPosition(), centerX, centerY);
			if (distance <= 2) {
				basicAttack();
				return true;
			}
			return false;
		}
		return super.attack();
	}

	@Override
	public boolean allowRetaliate(Entity attacker) {
		return false;
	}

}