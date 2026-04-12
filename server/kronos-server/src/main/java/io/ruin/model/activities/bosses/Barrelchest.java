package io.ruin.model.activities.bosses;

import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Projectile;
import io.ruin.utility.Misc;

public class Barrelchest extends NPCCombat {

	private static final Projectile PROJECTILE = new Projectile(475, 65, 31, 15, 56, 10, 15, 64);

	@Override
	public void init() {
	}


	@Override
	public boolean attack() {
		if (!withinDistance(8))
			return false;
		if (withinDistance(1) && Random.rollDie(3, 2))
			MeleeAttack();
		else if (Random.rollDie(5, 1))
			Jumpattack();
		else if (withinDistance(1))
			Deactivateattack();
		else
			MeleeAttack();
		return true;
	}

	@Override
	public void process() {

	}

	public void MeleeAttack() {
		npc.animate(5894);
		target.hit(new Hit(npc, AttackStyle.SLASH, null).randDamage(35));
	}

	public void Jumpattack() {
		npc.animate(5895);
		target.hit(new Hit(npc, AttackStyle.STAB, null).randDamage(40));
	}

	public void Deactivateattack() {
		npc.animate(5896);
		target.hit(new Hit(npc, AttackStyle.SLASH).randDamage(11).postDamage(entity -> {
			if (entity.player != null) {
				entity.player.getPrayer().deactivateAll();
				entity.player.sendMessage(Color.RED.wrap("Your prayers have been disabled!"));
			}
		}));
	}


	@Override
	public void follow() {
		follow(10);
	}

	@Override
	public int getAggressionRange() {
		return 8;
	}

	@Override
	public int getAttackBoundsRange() {
		return 20;
	}
}



