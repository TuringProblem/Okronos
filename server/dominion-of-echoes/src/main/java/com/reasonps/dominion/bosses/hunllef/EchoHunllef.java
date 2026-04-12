package com.reasonps.dominion.bosses.hunllef;

import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.Killer;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPC.DefaultHeadIconIndex;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.TickDelay;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import static core.api.ReasonUtils.pos;
import static io.ruin.model.map.Direction.*;

public class EchoHunllef extends NPCCombat {

	private static final Projectile CORRUPTED_PROJECTILE_MAGIC =
		new Projectile(1708, 40, 35, 17, 40, 6, 16, 192).regionBased();

	private static final Projectile CORRUPTED_RANGED_PROJECTILE =
		new Projectile(1712, 40, 35, 17, 40, 6, 16, 192).regionBased();

	private static final Projectile CORRUPTED_SMITE_PROJECTILE =
		new Projectile(1714, 40, 35, 17, 40, 6, 16, 192).regionBased();

	private Player player;

	private int offPrayerAttackDealt = 0;
	private int attackCounter = 0;
	private int standardAttackCounter = 0;

	public boolean damagedPlayer = false;
	public boolean hitOffPrayer = false;

	private AttackStyle lastBasicAttackStyle = AttackStyle.RANGED;

	private final List<NPC> tornadoes = new ArrayList<>();
	private final List<NPC> persistentTornadoes = new ArrayList<>();
	private final List<Position> tornadoSpawns = new ArrayList<>();

	private final TickDelay smiteAttackDelay = new TickDelay();
	private final TickDelay tornadoAttackDelay = new TickDelay();

	@Override
	public boolean allowRespawn() {
		return false;
	}

	@Override
	public void init() {
		npc.hitsUpdate.hpbarId = 1;
		npc.hitListener = new HitListener()
			.postDamage(this::postDamage)
			.preDefend(this::preHitDefend);

		var region = getNpc().getPosition().getRegion();
		tornadoSpawns.add(Position.of(region.baseX + 34, region.baseY + 50, 1));
		tornadoSpawns.add(Position.of(region.baseX + 45, region.baseY + 50, 1));
		tornadoSpawns.add(Position.of(region.baseX + 45, region.baseY + 61, 1));
		tornadoSpawns.add(Position.of(region.baseX + 34, region.baseY + 61, 1));

		startPersistentTornadoEvent();

		var overheads = List.of(
			DefaultHeadIconIndex.ProtectFromMelee,
			DefaultHeadIconIndex.ProtectFromRanged,
			DefaultHeadIconIndex.ProtectFromMagic
		);
		getNpc().setHeadIcon(Random.get(overheads));
	}

	@Override
	public void follow() {
		if (target == null) return;
		follow(6);
	}

	private void postDamage(Hit hit) {
		if (hit.attackStyle == null)
			return;

		if (hit.damage > 0)
			offPrayerAttackDealt++;

		if (offPrayerAttackDealt >= 6) {
			npc.animate(8754);
			updatePrayerOverheadForHit(hit);
			offPrayerAttackDealt = 0;
		}

		if (hit.attacker.isPlayer()) {
			if (!hit.attacker.player.getHealthHud().isOpened())
				hit.attacker.player.getHealthHud().open(true, npc.getId(), npc.getMaxHp());
			hit.attacker.player.getHealthHud().updateValue(npc.getHp());
		}
	}

	private void updatePrayerOverheadForHit(Hit hit) {
		switch (hit.attackStyle) {
			case MAGIC -> npc.setHeadIcon(DefaultHeadIconIndex.ProtectFromMagic);
			case RANGED -> npc.setHeadIcon(DefaultHeadIconIndex.ProtectFromRanged);
			default -> npc.setHeadIcon(DefaultHeadIconIndex.ProtectFromMelee);
		}
	}

	private void startTornadoEvent(Position spawnPosition) {
		if (npc == null) return;

		npc.animate(8420);
		int CORRUPTED_TORNADO = 17007;
		var tornado = new NPC(CORRUPTED_TORNADO).spawn(spawnPosition);
		tornadoes.add(tornado);
		var ticks = new AtomicInteger();

		tornado.startEvent(event -> {
			event.setCancelCondition(() -> getNpc().isRemoved() || isDead() || getNpc().getPosition().getRegion().players.isEmpty());
			while (ticks.getAndIncrement() < 30) {
				var target = npc.getCombat().getTarget();
				if (target == null) {
					tornado.remove();
					return;
				}

				var targetPos = target.getPosition();
				if (targetPos == null) {
					tornado.remove();
					return;
				}
				// Move tornado towards target
				tornado.stepAbs(targetPos.getX(), targetPos.getY(), StepType.WALK);
				// Check if tornado hits target
				if (tornado.getPosition().isAtPosition(targetPos)) {
					int randomDamage = Random.get(15, 30);
					damagedPlayer = true;
					target.player.hit(new Hit(npc).randDamage(randomDamage));
				}
				event.delay(1);
				// Check if tornado should be removed
				if (ticks.get() >= 30 || npc.getHp() < 1) {
					tornado.remove();
					return;
				}
			}
		});
	}

	private void startPersistentTornadoEvent() {
		if (npc == null) return;
		var region = getNpc().getPosition().getRegion();
		var spawnPosition =  Position.of(region.baseX + 34, region.baseY + 50, 1);
		npc.animate(8420);
		int CORRUPTED_TORNADO = 17007;
		var tornado = new NPC(CORRUPTED_TORNADO).spawn(spawnPosition);
		persistentTornadoes.add(tornado);
		var direction = new AtomicReference<>(EAST);
		World.startEvent(event -> {
			while (direction.get() != null) {
				var destination = getDestinationForDirection(direction.get());
				// Move tornado towards target
				tornado.stepAbs(destination.getX(), destination.getY(), StepType.WALK);
				// Check if tornado hits a wall
				if (tornado.getPosition().isAtPosition(destination)) {
					var currentDirection = direction.get();
					direction.set(switch (currentDirection) {
						case NORTH_WEST -> EAST;
						case NORTH -> SOUTH_EAST;
						case NORTH_EAST -> SOUTH;
						case WEST -> NORTH_EAST;
						case EAST -> SOUTH_WEST;
						case SOUTH_WEST -> NORTH;
						case SOUTH -> NORTH_WEST;
						case SOUTH_EAST -> WEST;
					});
				}
				// ull target check
				if (target != null && target.isPlayer()) {
					var targetPos = target.getPosition().copy();
					// are we and our target on the same tile?
					if (tornado.getPosition().isAtPosition(targetPos)) {
						int randomDamage = Random.get(18, 35);
						damagedPlayer = true;
						target.player.hit(new Hit(npc).randDamage(randomDamage));
					}
				}
				event.delay(1);
			}
		}).setCancelCondition(() -> getNpc().isRemoved() || isDead() || getNpc().getPosition().getRegion().players.isEmpty());
	}

	private Position getDestinationForDirection(Direction direction) {
		var region = getNpc().getPosition().getRegion();
		return switch (direction) {
			case WEST, SOUTH_WEST -> Position.of(region.baseX + 34, region.baseY + 50, 1);
			case SOUTH, SOUTH_EAST -> Position.of(region.baseX + 45, region.baseY + 50, 1);
			case EAST, NORTH_EAST -> Position.of(region.baseX + 45, region.baseY + 61, 1);
			case NORTH_WEST, NORTH -> Position.of(region.baseX + 34, region.baseY + 61, 1);
		};
	}

	private void preHitDefend(Hit hit) {
		if (hit.attackStyle != null) {
			if (hit.damage > 100)
				hit.damage = 100;
			var prayer = getNpc().overheadPrayer();
			switch (prayer) {
				case ProtectFromMagic:
					if (hit.attackStyle.isMagic()) {
						if (hit.attacker.player != null) {
							hit.block();
							damagedPlayer = true;
						}
					}
					break;
				case ProtectFromMelee:
					if (hit.attackStyle.isMelee()) {
						hit.block();
						damagedPlayer = true;
					}
					break;
				case ProtectFromRanged:
					if (hit.attackStyle.isRanged()) {
						hit.block();
						damagedPlayer = true;
					}
					break;
				case null:
					break;
				default:
					throw new IllegalArgumentException("Unexpected overhead protection prayer: ".concat(String.valueOf(prayer)));
			}
		}
	}

	public void startDeath(Killer killer, DynamicMap map) {
		tornadoes.forEach(NPC::remove);
		tornadoes.clear();

		persistentTornadoes.forEach(NPC::remove);
		persistentTornadoes.clear();

		attackCounter = 0;
		new NPC(17013)
			.spawn(pos(map, 38, 55, 1), Direction.NORTH);
		killer.player.getHealthHud().close();
	}

	List<DefaultHeadIconIndex> overheads = List.of(
		DefaultHeadIconIndex.ProtectFromMelee,
		DefaultHeadIconIndex.ProtectFromRanged,
		DefaultHeadIconIndex.ProtectFromMagic
	);
	@Override
	public boolean attack() {
		if (getNpc().overheadPrayer() == null)
			getNpc().setHeadIcon(Random.get(overheads));
		if (target == null)
			return false;
		npc.face(target);
		if (player == null)
			player = target.player;
		attackCounter++;
		// if the player is standing under the boss
		if (npc.getCentrePosition().distance(target.getPosition()) < 2)
			stomp();
		// else check if we can spawn some tornadoes to hit the player
		else if (tornadoAttackDelay.finished() && Random.get(0, 10) == 0) {
			int tornadoAmount = 1;
			if ((npc.getHp() / npc.getMaxHp()) * 100 < 65)
				tornadoAmount = 2;
			else if ((npc.getHp() / npc.getMaxHp()) * 100 < 40)
				tornadoAmount = 3;
			else if ((npc.getHp() / npc.getMaxHp()) * 100 < 20)
				tornadoAmount = 4;
			// start a tornado at a random position
			IntStream.range(0, tornadoAmount)
				.mapToObj(i -> Random.get(tornadoSpawns))
				.forEach(this::startTornadoEvent);
			// reset the delay
			tornadoAttackDelay.delay(35);
		}
		// check if we're diabling the player's prayers
		else if (attackCounter % 4 == 0 && Random.get() < 0.5)
			prayerDisableAttack();
			// else we're just auto-retaliating
		else
			standardAttack();

		return true;
	}

	@Override
	public void process() {
		if (npc != null) {
			var healthPercent = (npc.getHp() / npc.getMaxHp()) * 100;
			if (persistentTornadoes.size() == 1 && healthPercent <= 75)
				startPersistentTornadoEvent();
			if (persistentTornadoes.size() == 2 && healthPercent <= 50)
				startPersistentTornadoEvent();
			if (persistentTornadoes.size() == 3 && healthPercent <= 25)
				startPersistentTornadoEvent();
		}
	}

	public void prayerDisableAttack() {
		if (target == null) return;
		smiteAttackDelay.delay(50);
		npc.animate(8419);
		int delay = CORRUPTED_SMITE_PROJECTILE.send(npc, target);
		int maxDamage = 42;
		if (targetHasActiveProtectPrayersOn())
			target.player.hit(new Hit(npc, lastBasicAttackStyle)
				.randDamage(maxDamage)
				.ignorePrayer()
				.clientDelay(delay)
				.postDamage(t -> {
					if (t instanceof Player plr) {
						plr.graphics(1716, 0, delay);
						plr.getPrayer().deactivateAll();
						plr.sendMessage(Color.RED.wrap("Your prayers have been disabled!"));
					}
				})
			);
	}

	private boolean targetHasActiveProtectPrayersOn() {
		return target != null && (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES)
			|| target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)
			|| target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE));
	}

	public void stomp() {
		if (target == null) return;
		npc.animate(8420);
		target.hit(new Hit(npc, null, null)
			.fixedDamage(Random.get(30, 70))
			.ignoreDefence()
			.ignorePrayer());

		damagedPlayer = true;
	}

	public void standardAttack() {
		standardAttackCounter++;
		npc.animate(8419);
		if (target == null) return;
		int delay = (lastBasicAttackStyle == AttackStyle.RANGED ? CORRUPTED_RANGED_PROJECTILE : CORRUPTED_PROJECTILE_MAGIC)
			.send(npc, target);

		int maxDamage = info.max_damage;
		if (target.player.getPrayer().isActive(lastBasicAttackStyle == AttackStyle.RANGED ? Prayer.PROTECT_FROM_MISSILES : Prayer.PROTECT_FROM_MAGIC))
			maxDamage /= 3;
		else {
			hitOffPrayer = true;
			damagedPlayer = true;
		}
		target.player.hit(new Hit(npc, lastBasicAttackStyle)
			.randDamage(maxDamage)
			.clientDelay(delay));
		if (lastBasicAttackStyle == AttackStyle.MAGIC)
			target.graphics( 1704, 30, delay);

		if (standardAttackCounter % 4 == 0) {
			lastBasicAttackStyle = lastBasicAttackStyle == AttackStyle.RANGED ? AttackStyle.MAGIC : AttackStyle.RANGED;
		}
	}
}
