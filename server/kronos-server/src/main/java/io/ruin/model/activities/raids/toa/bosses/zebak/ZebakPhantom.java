package io.ruin.model.activities.raids.toa.bosses.zebak;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.activities.raids.toa.Invocations;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.prayer.Prayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ZebakPhantom extends NPCCombat {
	@Override
	public void init() {

	}

	@Override
	public void follow() {

	}

	@Override
	public boolean attack() {
		return false;
	}

	int ticksTilAttack = 0;

	@Override
	public void process() {
		if (ticksTilAttack-- <= 0) {
			ticksTilAttack = 5;
			handleAutoAttack();
		}
	}

	private void handleSwitchAttackStyle() {
		if (currentAttackStyle == AttackStyle.MAGIC) {
			currentAttackStyle = AttackStyle.RANGED;
		} else {
			currentAttackStyle = AttackStyle.MAGIC;
		}
	}

	AttackStyle currentAttackStyle = AttackStyle.MAGIC;

	private void handleAutoAttack() {
		//todo: projectile speed based on dist
		npc.animate(9624);
		AtomicInteger maxDamage = new AtomicInteger(34);
		if (Random.get(10) == 0) {
			handleSwitchAttackStyle();
		}
		World.startEvent(e -> {
			e.delay(2);
			npc.getPosition().getRegion().players.forEach(player -> {
				Position startPosition = new Position(npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ());
				int distance = startPosition.distance(player.getPosition());
				int projectileSpeed = 25;
				if (distance < 4) {
					projectileSpeed = 44;
					startPosition = new Position(npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ());
				}
				Projectile rangedProjectile = new Projectile(2187, 90, 30, 20, 15, projectileSpeed, 15, 10);
				Projectile magicProjectile = new Projectile(2181, 90, 30, 20, 15, projectileSpeed, 15, 10);

				boolean magic = false;
				if (currentAttackStyle == AttackStyle.MAGIC)
					magic = true;
				int delay2 = currentAttackStyle == AttackStyle.MAGIC ? magicProjectile.send(npc, startPosition.getX(), startPosition.getY(), player) : rangedProjectile.send(npc, startPosition.getX(), startPosition.getY(), player);
				e.delay(World.getTicks(delay2) + 1);
				int prayerMultiplier;
				if (player != null && player.getCurrentToARaid() != null && player.getCurrentToARaid().getInvocations().contains(Invocations.QUIET_PRAYERS))
					prayerMultiplier = 5;
				else {
					prayerMultiplier = 7;
				}
				if (magic && player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)) {
					maxDamage.updateAndGet(v -> v / prayerMultiplier);
				} else if (!magic && player.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES)) {
					maxDamage.updateAndGet(v -> v / prayerMultiplier);
				}
				if (!npc.isRemoved())
					player.hit(new Hit().randDamage(0, maxDamage.get()).ignorePrayer());
			});
		});
	}

}
