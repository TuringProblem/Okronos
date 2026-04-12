package io.ruin.model.activities.bosses;

import io.ruin.api.utils.Random;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Widget;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.prayer.Prayer;

public class TaintedKnight extends NPCCombat {

	private static final Projectile FIRE_SURGE_PROJECTILE = new Projectile(1465, 43, 31, 51, 56, 10, 16, 64);
	private static final Projectile ICE_BARRAGE_PROJECTILE = new Projectile(368, 43, 0, 51, 56, 10, 16, 64).skipTravel();
	private static final Projectile BLOOD_BARRAGE_PROJECTILE = new Projectile(51, 56, 10);
	private static final int SPECIAL_ATTACK_ANIMATION = 7515;
	private static final int SURGE_ANIMATION = 7855;
	private static final int BARRAGE_ANIMATION = 1979;
	private static final int ICE_BARRAGE_GFX = 369;
	private static final int BLOOD_BARRAGE_GFX = 377;
	private static final int SURGE_HIT_GFX = 1466;
	private static final int SURGE_CAST_GFX = 1464;
	Player player;
	boolean specialAttackStarted = false;
	int attacksSinceSpecial = 0;

	@Override
	public void init() {
		specialAttackStarted = false;
	}

	private void HandleSpecialAttack() {
		npc.startEvent(e -> {
			specialAttackStarted = true;
			iceBarrageAttack();
			e.delay(2);
			npc.lock();
			npc.animate(714);
			npc.graphics(111);
			e.delay(2);
			npc.getMovement().teleport(player.getPosition().getX() - 1, player.getPosition().getY(), player.getPosition().getZ());
			npc.animate(-1);
			npc.unlock();
			npc.face(target);
			e.delay(1);
			specialAttack();
		});
	}

	private void specialAttack() {
		npc.animate(SPECIAL_ATTACK_ANIMATION);
		int maxHit = 70;
		if (player.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE))
			maxHit /= 3;
		player.hit(new Hit(npc, AttackStyle.STAB).randDamage(10, maxHit).ignorePrayer());
		specialAttackStarted = false;
	}

	private void surgeAttack() {
		int delay = FIRE_SURGE_PROJECTILE.send(npc, player);
		npc.graphics(SURGE_CAST_GFX);
		npc.animate(SURGE_ANIMATION);
		int maxDamage = 60;
		if (player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
			maxDamage /= 3;
		player.hit(new Hit(npc, AttackStyle.MAGIC).randDamage(maxDamage).ignorePrayer().clientDelay(delay));
		player.graphics(SURGE_HIT_GFX, 92, delay);
	}

	void hold(Hit hit, Entity target, int seconds) {
		if (hit.isBlocked() || target.hasFreezeImmunity())
			return;
		target.freeze(seconds, hit.attacker);
		if (target.player != null) {
			target.player.sendMessage("You have been frozen.");
			target.player.getPacketSender().sendWidgetTimerCustom(Widget.BARRAGE, seconds);
		}
	}


	private void bloodBarrageAttack() {
		int delay = BLOOD_BARRAGE_PROJECTILE.send(npc, player);
		npc.animate(BARRAGE_ANIMATION);
		int maxDamage = 29;
		if (player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
			maxDamage /= 3;
		player.hit(new Hit(npc, AttackStyle.MAGIC).randDamage(maxDamage).ignorePrayer().clientDelay(delay));
		int healAmount = maxDamage / 2;
		npc.setHp(npc.getHp() + healAmount);
		npc.hit(new Hit(HitType.HEAL).fixedDamage(healAmount));
		player.graphics(BLOOD_BARRAGE_GFX, 0, delay);
	}

	private void iceBarrageAttack() {
		int delay = ICE_BARRAGE_PROJECTILE.send(npc, player);
		npc.animate(BARRAGE_ANIMATION);
		int maxDamage = 31;
		if (player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
			maxDamage /= 3;
		Hit hit = new Hit(npc, AttackStyle.MAGIC).randDamage(5, maxDamage).ignorePrayer().clientDelay(delay);
		player.hit(hit);
		player.graphics(ICE_BARRAGE_GFX, 0, delay);
		hold(hit, player, 15);
	}

	@Override
	public void follow() {

	}

	@Override
	public boolean attack() {
		if (player == null)
			player = target.player;
		npc.face(target);
		if (!specialAttackStarted) {
			if (npc.getPosition().isWithinDistance(player.getPosition(), 2))
				basicAttack();
			else if (Random.get(0, 7) == 0)
				HandleSpecialAttack();
			else if (npc.getHp() < (npc.getMaxHp() / 2) && Random.get(0, 4) == 0)
				bloodBarrageAttack();
			else
				surgeAttack();
		} else {
			attacksSinceSpecial++;
			if (attacksSinceSpecial >= 4)
				specialAttackStarted = false;
		}
		return true;
	}

	@Override
	public void process() {

	}
}
