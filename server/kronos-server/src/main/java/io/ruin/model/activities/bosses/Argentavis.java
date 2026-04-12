package io.ruin.model.activities.bosses;

import io.ruin.api.utils.Random;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.inter.Widget;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.Tile;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.TickDelay;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Argentavis extends NPCCombat {
	private static final Projectile MAGIC_PROJECTILE = new Projectile(1642, 90, 43, 35, 40, 6, 16, 192).regionBased();
	private static final Projectile RANGED_PROJECTILE = new Projectile(1607, 90, 43, 35, 40, 6, 16, 192).regionBased();
	private AttackStyle lastBasicAttackStyle = Random.rollPercent(50) ? AttackStyle.MAGIC : AttackStyle.RANGED;
	ArrayList<NPC> tornado = new ArrayList<>();
	private final int TORNADO_GFX = 1608;
	private final int TORNADO_NPC_ID = 20015;
	int minionsSpawned = 0;
	List<NPC> spawnedMinions = new ArrayList<>();
	boolean canAttack = true;
	boolean canPickup = true;
	boolean pickupStarted = false;
	boolean canSpawnMinions = true;

	public boolean playerDamagedByNado = false;
	boolean canSpawnTornados = true;
	TickDelay pickupDelay = new TickDelay();
	TickDelay minionsDelay = new TickDelay();
	TickDelay tornadoDelay = new TickDelay();
	List<NPC> minions = new ArrayList<>();
	int ticksNotAttackable = 0;

	// private final Bounds argentavisLair = new Bounds(2131, 5523, 2159, 5546, 3);
	// private final Bounds argentavisInnerLair = new Bounds(2139, 5531, 2148, 5537, 3);

	public boolean timerStarted = false;

	@Override
	public void init() {
		npc.hitListener = new HitListener().postDamage(this::postDamage).preDefend(this::preHitDefend);
		pickupStarted = false;
	}

	@Override
	public void follow() {}

	@Override
	public boolean attack() {
		if (!timerStarted) {
			timerStarted = true;
			target.player.argentavisTimer = new ActivityTimer();
		}
		npc.face(getTarget());
		int random = Random.get(0, 9);
		//if(random == 0 && canPickup)
		//  pickupAttack();
		if (random == 1 && canSpawnMinions)
			spawnMinions();
		else if (random == 2 && canSpawnTornados)
			Tornados();
		else
			basicAttack(npc.getPosition().getRegion().players);
		return true;
	}

	int npcSize() {
		int size = 0;
		for (NPC n : npc.localNpcs()) {
			if (n.getId() == 762)
				size++;
		}
		return size;
	}

	@Override
	public int getAggressionRange() {
		return 32;
	}

	@Override
	public int getAttackBoundsRange() {
		return 32;
	}

	@Override
	public void process() {
		if (target == null && npc.getHp() >= npc.getMaxHp())
			playerDamagedByNado = false;
		if (npcSize() == 0) {
			spawnedMinions.clear();
		}
		spawnedMinions.removeIf(minion -> minion.getCombat().isDead());
		spawnedMinions.removeIf(Objects::isNull);
		spawnedMinions.removeIf(Entity::isHidden);
		spawnedMinions.removeIf(Entity::dead);
		spawnedMinions.removeIf(minion -> npc.getPosition().getRegion().players.isEmpty());
		canAttack = spawnedMinions.size() == 0;


		if (pickupDelay.remaining() <= 0)
			canPickup = true;

		if (minionsDelay.remaining() <= 0)
			canSpawnMinions = true;

		if (tornadoDelay.remaining() <= 0)
			canSpawnTornados = true;

		if (npc.getCombat().getTarget() == null) {
			if (playersInArea() > 0)
				npc.getCombat().setTarget(Random.get(npc.getPosition().getRegion().players));
		}
		for (int i = minions.size() - 1; i >= 0; i--) {
			NPC minion = minions.get(i);
			if (minion.getHp() <= 0 || minion.dead() || minion.isHidden())
				minions.remove(i);
		}
		if (minions.size() < 1)
			canAttack = true;

		if (ticksNotAttackable > 30) {
			canAttack = true;
			ticksNotAttackable = 0;
		}

		ticksNotAttackable++;

	}

	private void postDamage(Hit hit) {

	}

	private void preHitDefend(Hit hit) {
		if (!canAttack && hit.attacker != null) {
			hit.attacker.player.sendMessage("You must kill all the minions before attacking the Argentavis again!");
			hit.block();
		}
	}


	public int playersInArea() {
		AtomicInteger players = new AtomicInteger();
		npc.getPosition().getRegion().players.forEach(p -> {
			players.getAndIncrement();
		});
		return players.get();
	}


	void hold(Entity target, int seconds) {
		target.freeze(seconds, npc);
		if (target.player != null) {
			target.player.getPacketSender().sendWidgetTimerCustom(Widget.BARRAGE, seconds);
		}
	}

	private void pickupAttack() {
		pickupStarted = true;
		canPickup = false;
		Player randomTarget = Random.get(npc.localPlayers());
		AtomicBoolean caught = new AtomicBoolean(false);
		randomTarget.sendMessage("The argentavis has chosen you as a target, you have frozen in fear.");
		hold(randomTarget, 6);
		npc.startEvent(p -> {
			p.delay(1);
			npc.animate(5025);
			p.delay(1);
			npc.face(randomTarget);
			npc.getRouteFinder().routeAbsolute(randomTarget.getPosition().getX(), randomTarget.getPosition().getY());
			p.delay(2);
			npc.animate(5024);
			p.delay(1);
			npc.getMovement().teleport(0, 0, 0);
			randomTarget.getMovement().teleport(2789, 2599, 2);
			randomTarget.sendMessage("You have been picked up and dropped for a high distance by the Argentavis!");
			p.delay(1);
			randomTarget.animate(768);

			//  npc.getMovement().teleport(randomPos.getX(), randomPos.getY(), randomPos.getZ());
			if (playersInArea() > 0) {
				npc.getCombat().setTarget(Random.get(npc.localPlayers()));
				npc.attackTargetPlayer();
			}
			npc.animate(5028);
			// NPC shadow = new NPC(20018).spawn(shadowPos.getX(), shadowPos.getY(), shadowPos.getZ(), 0);
			for (int i = 0; i < 6; i++) {
				randomTarget.animate(768);
				p.delay(1);
			}
			npc.localPlayers().forEach(t -> {
				//  if(t.getPosition().distance(shadowPos) <= 0)
				// caught.set(true);
			});
			if (caught.get()) {
				randomTarget.hit(new Hit(npc).randDamage(0, 0));
				randomTarget.sendMessage("You were caught saving you from being seriously hurt!");
			} else {
				randomTarget.hit(new Hit(npc).randDamage(50, 75));
				randomTarget.sendMessage("The argentavis drops you from the sky dealing massive damage!");
			}
			// randomTarget.getMovement().teleport(shadowPos.getX(), shadowPos.getY(), shadowPos.getZ());
			p.delay(1);
			randomTarget.animate(767);
			// shadow.remove();
			randomTarget.resetFreeze();
			pickupStarted = false;

		});
		pickupDelay.delaySeconds(45);

	}


	private void spawnMinions() {
		minionsDelay.delaySeconds(90);
		canSpawnMinions = false;
		int minionAmount = playersInArea();
		minionsSpawned = minionAmount;
		minions.clear();
		npc.getPosition().getRegion().players.forEach(p -> {
			Position pos = npc.getPosition();

			NPC minion = new NPC(762).spawn(pos.getX(), pos.getY(), p.getPosition().getZ(), 1);
			minions.add(minion);
			minion.face(p);
			minion.getCombat().setTarget(p);
			minion.attackTargetPlayer();
			spawnedMinions.add(minion);
			minion.getCombat().setAllowRespawn(false);
		});
	}

	private void Tornados() {
		npc.animate(5031);
		canSpawnTornados = false;
		npc.getPosition().getRegion().players.forEach(p -> {
			Position pos = null;
			for (int dx = -3; dx <= 3; dx++) {
				for (int dy = -3; dy <= 3; dy++) {
					Position potentialPosition = p.getPosition().copy().translate(dx, dy, 0);
					if (!potentialPosition.equals(p.getPosition()) && Tile.get(potentialPosition, true).clipping == 0) {
						pos = potentialPosition;
						break;
					}
				}
			}
			if (pos == null) {
				return;
			}
			NPC tornado = new NPC(8921).spawn(pos);
			AtomicInteger ticks = new AtomicInteger();
			tornado.startEvent(event -> {
				while (ticks.getAndIncrement() < 18) {
					tornado.animate(-1);
					tornado.graphics(TORNADO_GFX);
					tornado.stepAbs(p.getPosition().getX(), p.getPosition().getY(), StepType.WALK);
					if (tornado.getPosition().isWithinDistance(p.getPosition(), 1)) {
						playerDamagedByNado = true;
						p.hit(new Hit(npc).randDamage(10, 17));
					}
					event.delay(1);

					if (ticks.get() >= 18 || npc.getHp() < 1) {
						tornado.remove();
						tornadoDelay.delaySeconds(60);
					}
				}
			});
		});
	}

	private void basicAttack(List<Player> targets) {
		lastBasicAttackStyle = Random.rollPercent(75) ? lastBasicAttackStyle : (lastBasicAttackStyle == AttackStyle.RANGED ? AttackStyle.MAGIC : AttackStyle.RANGED);
		targets.forEach(p -> {
			int delay = (lastBasicAttackStyle == AttackStyle.RANGED ? RANGED_PROJECTILE : MAGIC_PROJECTILE).send(npc, p);
			npc.animate(5023);
			int maxDamage = info.max_damage;

			p.hit(new Hit(npc, lastBasicAttackStyle).randDamage(maxDamage).clientDelay(delay));
		});
	}
}
