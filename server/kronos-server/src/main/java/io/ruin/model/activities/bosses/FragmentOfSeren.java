package io.ruin.model.activities.bosses;

import io.ruin.api.utils.Random;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Projectile;
import io.ruin.utility.TickDelay;

public class FragmentOfSeren extends NPCCombat {
	private static final Projectile MAGIC_PROJECTILE = new Projectile(1382, 90, 31, 35, 35, 10, 0, 32);
	private static final Projectile RANGED_PROJECTILE = new Projectile(1700, 20, 15, 35, 35, 10, 0, 32);


	private static final int MAGIC_ANIM = 8376;
	private static final int RANGED_ANIM = 8380;

	//    private static final int TELEPORT_ANIM = 8376;
//    private static final int SURFACE_ANIM = 8380;
	private TickDelay comboAttackCooldown = new TickDelay();


	@Override
	public void init() {
		npc.hitListener = new HitListener().postDefend(hit -> {
		});
		npc.deathEndListener = (entity, killer, killHit) -> {
			for (NPC n : entity.localNpcs()) {
			}
		};

	}


	@Override
	public void follow() {
		follow(30);
	}

	@Override
	public boolean attack() {
		if (!withinDistance(16))
			return false;
		if (Random.rollDie(6, 1))
			RangedAttack();
		else
			MagicAttack();
		return true;
	}

	@Override
	public void process() {

	}


	public void RangedAttack() {
		npc.face(target);
		npc.addEvent(event -> {
			npc.animate(RANGED_ANIM);
			event.delay(2);
			int delay = RANGED_PROJECTILE.send(npc, target);
			Hit hit = new Hit(npc, AttackStyle.RANGED)
				.randDamage(info.max_damage)
				.clientDelay(delay);
			hit.postDamage(t -> {
				if (hit.damage > 0) {
					npc.hit(new Hit(HitType.HEAL).fixedDamage(20));
				}
			});
			target.hit(hit);
		});
	}

	public void MagicAttack() {
		npc.addEvent(event -> {
			npc.face(target);
			npc.animate(MAGIC_ANIM);
			event.delay(1);
			int delay = MAGIC_PROJECTILE.send(npc, target);
			Hit hit = new Hit(npc, AttackStyle.MAGIC)
				.randDamage(info.max_damage)
				.clientDelay(delay);
			hit.postDamage(t -> {
				if (hit.damage > 0) {
					npc.hit(new Hit(HitType.HEAL).fixedDamage(20));
				}
			});
			target.hit(hit);

		});
	}

//    public void Teleport() {
//        npc.addEvent(event -> {
//            npc.face(target);
//                reset();
//                npc.animate(TELEPORT_ANIM);
//                event.delay(3);
//                npc.getMovement().teleport(TELEPORT_LOC);
//                npc.animate(SURFACE_ANIM);
//                event.delay(2);
//
//        });
//    }


}
