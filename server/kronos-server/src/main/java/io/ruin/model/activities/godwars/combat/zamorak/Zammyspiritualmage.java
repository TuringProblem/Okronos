package io.ruin.model.activities.godwars.combat.zamorak;


import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.map.Projectile;

public class Zammyspiritualmage extends NPCCombat {

	private int attackStyleRange;

	@Override
	public void init() {
		attackStyleRange = getAttackStyle().isMelee() ? 1 : getAttackStyle().isMagicalMelee() ? 1 : 8;


	}

	@Override
	public void follow() {
		follow(attackStyleRange);
	}

	@Override
	public boolean attack() {
		if (!withinDistance(attackStyleRange))
			return false;
		basicAttack();
		return true;
	}

	@Override
	public void process() {

	}

}





