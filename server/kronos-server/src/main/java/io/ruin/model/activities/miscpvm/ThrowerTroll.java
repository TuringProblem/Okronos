package io.ruin.model.activities.miscpvm;

import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.map.Projectile;

import java.util.Arrays;
import java.util.List;

public class ThrowerTroll extends NPCCombat {

	private static final Projectile PROJECTILE = new Projectile(850, 20, 15, 30, 30, 10, 16, 64);

	@Override
	public void init() {

	}

	@Override
	public void follow() {
		follow(8);
	}

	@Override
	public boolean attack() {
		if (!withinDistance(8))
			return false;
		projectileAttack(PROJECTILE, info.attack_animation, info.attack_style, info.max_damage);
		return true;
	}

	@Override
	public void process() {

	}
}
