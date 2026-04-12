package io.ruin.model.activities.bosses;

import io.ruin.api.utils.Random;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Projectile;
import io.ruin.utility.Misc;


public class Maledictus extends NPCCombat {


	private static final Projectile RANGED_PROJECTILE = new Projectile(2033, 100, 31, 50, 65, 6, 0, 255);
	private static final Projectile ICE_PROJECTILE = new Projectile(159, 100, 31, 50, 65, 6, 0, 255);

	@Override
	public void init() {
		npc.hitListener = new HitListener().postDefend(this::postDefend);
	}

	private void postDefend(Hit hit) {
		if (hit.damage > 100 && !hit.isBlocked())
			hit.damage = 100;
	}

	@Override
	public void updateLastDefend(Entity attacker) {
		super.updateLastDefend(attacker);
		super.updateLastDefend(attacker);
		if (attacker.player != null && !attacker.player.getCombat().isSkulled()) {
			attacker.player.getCombat().skullNormal();
			attacker.player.sendMessage("<col=6f0000>You've been marked with a skull for attacking Maledictus!");
		}
	}

	@Override
	public void follow() {
		follow(6);
	}

	@Override
	public boolean attack() {
		if (!withinDistance(8))
			return false;
		if (Random.rollDie(5, 1)) {
			magicAttack();
		} else if (Random.rollPercent(50)) {
			rangedAttack();
		} else if (withinDistance(1)) {
			basicAttack();
		}
		return true;
	}

	@Override
	public void process() {

	}

	private void magicAttack() {
		npc.animate(9278);
		npc.graphics(25, 250, 0);
		int delay = ICE_PROJECTILE.send(npc, target);//159
		target.graphics(2035, 0, delay);
		target.hit(new Hit(npc, AttackStyle.MAGIC).randDamage(info.max_damage).clientDelay(delay).postDamage(entity -> {
			entity.freeze(5, npc);
			if (entity.player != null) {
				entity.player.sendMessage("The ice freezes you to the bone!");
			}
			entity.localPlayers().forEach(p -> {
				if (p == entity)
					return;
				if (Misc.getDistance(entity.getPosition(), p.getPosition()) <= 1) {
					p.graphics(1312, 0, 0);
					p.hit(new Hit(npc, AttackStyle.MAGIC).randDamage((int) (info.max_damage * 2.0 / 3)));
				}
			});
		}));
	}

	private void rangedAttack() {
		npc.animate(9282);
		npc.localPlayers().forEach(p -> {
			if (!canAttack(p) || Misc.getEffectiveDistance(npc, p) > 8)
				return;
			int delay = RANGED_PROJECTILE.send(npc, p);
			target.graphics(382, 0, delay);
			p.hit(new Hit(npc, AttackStyle.RANGED).randDamage(info.max_damage).clientDelay(delay));
		});
	}
}
