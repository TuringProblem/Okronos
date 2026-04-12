package io.ruin.model.activities.bosses;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.item.actions.impl.combine.SlayerHelm;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.route.routes.DumbRoute;
import io.ruin.model.skills.slayer.Slayer;
import io.ruin.utility.Misc;

import java.util.List;

public class LizardmanShaman extends NPCCombat {

	private static final int RANGED_ANIMATION = 7193;
	private static final int JUMP_ANIMATION = 7152;
	private static final int LAND_ANIMATION = 6946;
	private static final int SUMMON_ANIIMATION = 7157;

	private static final int SPAWN = 6768;


	private static final Projectile RANGED_PROJECTILE = new Projectile(1291, 120, 30, 50, 56, 14, 5, 10);
	private static final Projectile ACIDIC_PROJECTILE = new Projectile(1293, 105, 1, 61, 75, 10, 30, 128);


	@Override
	public void init() {

	}

	@Override
	public void follow() {
		follow(5);
	}

	@Override
	public boolean attack() {
		if (!withinDistance(7))
			return false;
//        if (Random.rollDie(5, 1))
//            jumpAttack();
		if (Random.rollDie(5, 1))
			spawns();
		else if (Random.rollDie(3, 1))
			acidicAttack();
		else if (withinDistance(1) && Random.rollDie(10, 6))
			basicAttack();
		else
			rangedAttack();
		return true;
	}

	@Override
	public void process() {

	}

	private void rangedAttack() {
		npc.animate(RANGED_ANIMATION);
		int delay = RANGED_PROJECTILE.send(npc, target);
		Hit hit = new Hit(npc, AttackStyle.RANGED).randDamage(info.max_damage).clientDelay(delay);
		target.hit(hit);
	}

	private void jumpAttack() {
		if (target.isLocked())
			return;
		npc.startEvent(event -> {
			Position destination = target.getPosition().copy().translate(0, -1, 0);
			Bounds hitBounds = new Bounds(target.getPosition(), 1);
			npc.animate(JUMP_ANIMATION);
			npc.lock(LockType.FULL_DELAY_DAMAGE);
			target.getCombat().reset();
			event.delay(2);
			npc.getMovement().teleport(destination);
			event.delay(2);
			npc.animate(LAND_ANIMATION);
			for (Player player : npc.localPlayers()) {
				if (player.getPosition().inBounds(hitBounds))
					player.hit(new Hit(npc, AttackStyle.CRUSH, null).randDamage(20, 25).ignoreDefence().ignorePrayer());
			}
			event.delay(1);
			npc.unlock();
		});
	}

	public static int HELM = 13379;
	public static int PLATE = 13381;
	public static int LEGS = 13380;
	public static int GLOVES = 13377;
	public static int BOOTS = 13378;

	public int minDamage = 25;
	public int maxDamage = 30;
	public int poisonDamage = 10;

	public int min = 0;
	public int max = 0;
	public int poison = 0;

	private void acidicAttack() {
		int count = 0;
		if (target.player.getEquipment().getId(Equipment.SLOT_HAT) == HELM || Slayer.hasSlayerHelmEquipped(target.player)) {
			count += 1;
		}
		if (target.player.getEquipment().getId(Equipment.SLOT_CHEST) == PLATE) {
			count += 1;
		}
		if (target.player.getEquipment().getId(Equipment.SLOT_LEGS) == LEGS) {
			count += 1;
		}
		if (target.player.getEquipment().getId(Equipment.SLOT_HANDS) == GLOVES) {
			count += 1;
		}
		if (target.player.getEquipment().getId(Equipment.SLOT_FEET) == BOOTS) {
			count += 1;
		}
		if (count == 5) {
			min = 0;
			max = 0;
			poison = 0;
		} else if (count == 4) {
			min = 5;
			max = 6;
			poison = 2;
		} else if (count == 3) {
			min = 10;
			max = 12;
			poison = 4;
		} else if (count == 2) {
			min = 15;
			max = 18;
			poison = 6;
		} else if (count == 1) {
			min = 20;
			max = 24;
			poison = 8;
		} else if (count == 0) {
			min = 25;
			max = 30;
			poison = 10;
		}
		int count1 = count;
		Position destination = target.getPosition().copy();
		npc.animate(RANGED_ANIMATION);
		int delay = ACIDIC_PROJECTILE.send(npc, destination.getX(), destination.getY());
		World.sendGraphics(1294, 1, delay, destination.getX(), destination.getY(), destination.getZ());
		World.startEvent(event -> {
			event.delay((delay * 25 / 600) - 1);
			for (Player p : npc.localPlayers()) {
				if (p.getPosition().isWithinDistance(destination, 1)) {
					p.hit(new Hit(npc, AttackStyle.MAGIC, null).randDamage(min, max).ignoreDefence().ignorePrayer());
//                       System.err.println(min + " " + max + " " + poison + " " + count1);
					if (Random.rollDie(2, 1) && (count1 != 5))
						p.poison(poison);
				}
			}
		});
	}

	private void spawns() {
		if (target.isLocked())
			return;
		final Entity e = target;
		npc.animate(SUMMON_ANIIMATION);
		npc.addEvent(event -> {
			event.delay(1);
			int amount = Random.get(2, 3);
			List<Position> positions = e.getPosition().area(2, pos -> !pos.equals(e.getPosition()) && (pos.getTile() == null || pos.getTile().clipping == 0));
			for (int i = 0; i < amount; i++) {
				NPC spawn = new NPC(SPAWN).spawn(Random.get(positions));
				spawn.startEvent(spawnEvent -> {
					spawn.face(e);
					int boomDelay = Random.get(3, 5);
					while (boomDelay > 0) {
						boomDelay--;
						if (!e.getCombat().isDead() && Misc.getDistance(e.getPosition(), spawn.getPosition()) < 12) {
							DumbRoute.step(spawn, e, 1);
						}
						spawnEvent.delay(1);
					}
					spawn.getMovement().reset();
					spawn.animate(7159);
					spawnEvent.delay(4);
					World.sendGraphics(1295, 0, 0, spawn.getPosition());
					spawn.localPlayers().forEach(p -> {
						int distance = Misc.getDistance(p.getPosition(), spawn.getPosition());
						if (distance <= 2) {
							p.hit(new Hit(null, null).fixedDamage(10 - distance).ignoreDefence().ignorePrayer());
						}
					});
					spawn.remove();
				});
			}
		});
	}
}
