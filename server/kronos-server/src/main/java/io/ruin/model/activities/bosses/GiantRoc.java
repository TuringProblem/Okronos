package io.ruin.model.activities.bosses;

import io.ruin.api.utils.Random;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Projectile;
import io.ruin.utility.Misc;

public class GiantRoc extends NPCCombat {

	private static final Projectile PROJECTILE = new Projectile(475, 65, 31, 15, 56, 10, 15, 64);

	@Override
	public void init() {
	}


	@Override
	public boolean attack() {
		if (!withinDistance(12))
			return false;

		if (Random.rollDie(2))
			MeleeAttack();

		else if (Random.rollDie(1))
			Rangedattack();
		return true;
	}

	@Override
	public void process() {

	}

	public void MeleeAttack() {
		npc.animate(5023);
//            target.hit(new Hit(npc, AttackStyle.SLASH, null).randDamage(info.max_damage));
		target.hit(new Hit(npc, AttackStyle.SLASH, null).randDamage(55));
	}

	public void Rangedattack() {
		npc.animate(5025);
		int delay = PROJECTILE.send(npc, target);
		target.hit(new Hit(npc, AttackStyle.RANGED).randDamage(41).clientDelay(delay));
		for (Player p : target.localPlayers()) {
			if (!canAttack(p) || Misc.getDistance(npc.getPosition(), p.getPosition()) > 6)
				continue;
			delay = PROJECTILE.send(npc, p);
			p.hit(new Hit(npc, AttackStyle.RANGED).randDamage(21).clientDelay(delay));
		}
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



