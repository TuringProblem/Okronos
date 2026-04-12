package com.reasonps.dominion.bosses.kbd;

import io.ruin.api.utils.Random;
import io.ruin.model.activities.miscpvm.dragons.ChromaticDragon;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.map.Projectile;

public class EchoBlackDragon extends ChromaticDragon {

	static final Projectile RANGED_DRAGONFIRE = new Projectile(393, 37, 32, 50, 60, 5, 24, 200);

	boolean fire;

	@Override
	public void follow() {
		follow(8);
	}

	@Override
	public int getAggressionRange() {
		return 32;
	}

	@Override
	public int getAttackBoundsRange() {
		return 32;
	}

	@Override
	public void init() {
		npc.hitsUpdate.hpbarId = 1;
	}

	@Override
	public boolean attack() {
		if (!withinDistance(8))
			return false;
		if (!withinDistance(1) || (!fire && Random.rollDie(3, 1)))
			rangedDragonfire();
		else if (Random.rollDie(6, 1))
			meleeDragonfire();
		else
			basicAttack();
		return true;
	}

	@Override
	public boolean allowRespawn() {
		return false;
	}

	void rangedDragonfire() {
		fire = true;
		projectileAttack(RANGED_DRAGONFIRE, 81, AttackStyle.DRAGONFIRE, 50);
	}

}
