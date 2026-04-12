package io.ruin.model.activities.bosses;

import io.ruin.api.utils.Random;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.prayer.Prayer;

public class Necrolord extends NPCCombat {

	private static final Projectile SHADOW_BARRAGE = new Projectile(56, 10);
	private static final Projectile BLOOD_BARRAGE = new Projectile(366, 60, 0, 51, 65, 0, 16, 127);

	@Override
	public void init() {

	}

	@Override
	public void follow() {
		follow(8);
	}

	@Override
	public boolean attack() {
		if (!withinDistance(10))
			return false;
		if (target.player != null) {
			if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES)) {
				shadowBarrage();
				return true;
			} else if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)) {
				bloodBarrage();
				return true;
			}
		}
		if (Random.rollDie(2, 1))
			bloodBarrage();
		else
			shadowBarrage();
		return true;
	}

	@Override
	public void process() {

	}

	private void shadowBarrage() {
		npc.animate(1979);
		Position targetPosition = target.getPosition().copy();
		int delay = SHADOW_BARRAGE.send(npc, targetPosition.getX(), targetPosition.getY());
		npc.addEvent(event -> {
			event.delay(delay * 25 / 600 - 1);
			npc.localPlayers().forEach(p -> {
				if (canAttack(p) && p.getPosition().equals(targetPosition)) {
					int maxDamage = 50;
					if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES)) {
						maxDamage /= 2;
					}
					p.hit(new Hit(npc, AttackStyle.RANGED).randDamage(maxDamage).ignorePrayer());
					target.player.getPrayer().drain(Random.get(2, 5));
					p.graphics(383, 0, 0);
				}
			});
		});
	}

	private void bloodBarrage() {
		npc.animate(1979);
		Position targetPosition = target.getPosition().copy();
		int delay = BLOOD_BARRAGE.send(npc, targetPosition.getX(), targetPosition.getY());
		npc.addEvent(event -> {
			event.delay(delay * 25 / 600 - 1);
			npc.localPlayers().forEach(p -> {
				if (canAttack(p) && p.getPosition().isWithinDistance(targetPosition, 1)) {
					int maxDamage = 50;
					if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)) {
						maxDamage /= 2;
					}
					p.hit(new Hit(npc, AttackStyle.RANGED).randDamage(maxDamage).ignorePrayer());
					npc.hit(new Hit(HitType.HEAL).randDamage(10, 25));
					p.graphics(377, 0, 0);
				}
			});
		});
	}
}
