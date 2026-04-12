package io.ruin.model.activities.raids.tob.dungeon.boss.maiden;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.activities.raids.tob.party.TheatreParty;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.TickDelay;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


public class MaidenCombat extends NPCCombat {

	public TheatreParty party;

	private final int bloodPitGFX = 1579;
	private final int bloodPitAnim = 8091;

	public boolean perfectMaidenFailed = false;

	private final Projectile bloodProjectile = new Projectile(1578, 20, 31, 20, 15, 12, 15, 10).regionBased();

	private final int TORNADO_ANIM = 8092;
	private final Projectile tornadoProjectile = new Projectile(1577, 20, 31, 20, 15, 12, 15, 10).regionBased();
	private int stage = 0;
	List<Position> poolPositions = new ArrayList<>();

	boolean poolsCanSpawn = true;

	public boolean wearingBarrows = true;

	List<BloodNylo> bloodNylos = new ArrayList<>();

	List<Position> bloodSpawns = new ArrayList<>();


	public boolean scanning = true;

	TickDelay nyloDelay = new TickDelay();

	@Override
	public void init() {
		npc.hitListener = new HitListener().postDamage(hit -> {
			if (hit.attacker != null && hit.attacker.isPlayer()) {
				if (hit.attacker instanceof Player)
					hit.attacker.player.tobDamageDealt += hit.damage;
			}
			double ratio = ((double) npc.getHp() / npc.getMaxHp());
			if (ratio <= .70 && stage == 0 && nyloDelay.remaining() < 1) {
				npc.transform(8361);
				nylocas();
			} else if (ratio <= .50 && stage == 1 && nyloDelay.remaining() < 1) {
				npc.transform(8362);
				nylocas();
			} else if (ratio <= .3 && stage == 2 && nyloDelay.remaining() < 1) {
				npc.transform(8363);
				nylocas();
			} else if (ratio == 0) {
				npc.transform(8364);
			}
		});
		npc.deathEndListener = (entity, killer, hit) -> {
			bloodNylos.stream()
				.filter(Objects::nonNull)
				.map(nylo -> nylo.npc)
				.forEach(NPC::remove);
			bloodNylos.clear();
		};
	}

	@Override
	public void follow() {

	}

	public boolean cantDrainThisFailed = false;


	@Override
	public boolean attack() {
		AtomicInteger nearestDistance = new AtomicInteger(100);
		for (Player p : npc.getPosition().getRegion().players) {
			if (npc.getPosition().distance(p.getPosition()) < nearestDistance.get()) {
				nearestDistance.set(npc.getPosition().distance(p.getPosition()));
				target = p;
			}
		}
		int randomNumber = Random.get(1, 10);
		tornadoAttack(target, npc);
		if (randomNumber < 4)
			bloodSpatAttack(npc);
		if (randomNumber >= 9 && poolsCanSpawn) {
			poolsCanSpawn = false;
			bloodSpawnAttack();
		}
		return true;
	}

	@Override
	public void process() {
		if (!npc.getPosition().getRegion().players.isEmpty()) {
			for (Player p : npc.getPosition().getRegion().players) {
				if (!p.wearingBarrows())
					wearingBarrows = false;
			}
		}
		for (BloodNylo bloodNylo :
			bloodNylos) {
			bloodNylo.process(npc);
		}
		npc.getPosition().getRegion().players.forEach(p -> {
			if (p.currentParty == null || p.currentParty.deadPlayers.contains(p))
				return;
			p.startEvent(event -> {
				for (int i = 0; i < bloodSpawns.size(); i++) {
					World.sendGraphics(bloodPitGFX, 0, 0, bloodSpawns.get(i).getX(), bloodSpawns.get(i).getY(), bloodSpawns.get(i).getZ());
					if (p.getPosition().distance(bloodSpawns.get(i)) < 1) {
						int heal = Random.get(8, 20);
						p.hit(new Hit(HitType.DAMAGE).randDamage(heal));
						p.getPrayer().drain(Random.get(4, 7));
						cantDrainThisFailed = true;
						perfectMaidenFailed = true;
						heal *= 4;
						npc.hit(new Hit(HitType.HEAL).fixedDamage(heal));
						event.delay(1);
					}
				}
			});
		});
	}


	private void bloodSpatAttack(NPC npc) {
		for (Player p : npc.getPosition().getRegion().players) {
			if (p.getPosition().isWithinDistance(npc.getPosition(), 20)) {
				npc.animate(bloodPitAnim);
				Position pos = p.getPosition().copy();
				int delay = bloodProjectile.send(npc, pos);
				World.startEvent(event -> {
					event.delay(World.getTicks(delay) + 1); // Send 1 blood splat to each player
					World.sendGraphics(bloodPitGFX, 0, 0, pos.getX(), pos.getY(), pos.getZ());
					bloodSpawns.add(pos);
				});
			}
		}
	}

	private void bloodSplatHandler(Position pos) {
		Position poolPosition = pos.copy();
		World.sendGraphics(bloodPitGFX, 0, 0, poolPosition.getX(), poolPosition.getY(), poolPosition.getZ());
		bloodSpawns.add(poolPosition);
	}

	public boolean anticoagulants = false;

	private void bloodSpawnAttack() {
		Position bloodSpawnPos = new Position(target.getPosition().getX() + Random.get(0, 1),
			target.getPosition().getY() + Random.get(-3, 3), target.getPosition().getZ());

		NPC bloodSpawn = new NPC(8367).spawn(bloodSpawnPos);
		bloodSpawn.getCombat().setAllowRetaliate(false);
		ActivityTimer timer = new ActivityTimer();
		bloodSpawn.startEvent(e -> {
			for (int i = 0; i < 30; i++) {
				if (!anticoagulants && ActivityTimer.timeInSeconds(timer.getTime()) >= 10 && bloodSpawn.getHp() > 0) {
					anticoagulants = true;
				}
				e.waitForMovement(bloodSpawn);
				bloodSplatHandler(bloodSpawn.getPosition().copy());
				bloodSpawn.getRouteFinder().routeAbsolute(bloodSpawn.getPosition().getX() + Random.get(-1, 1), bloodSpawn.getPosition().getY() + Random.get(-1, 1));
				if (i == 29) {
					bloodSpawn.remove();
				}
			}
		});
	}

	private void nylocas() {
		nyloDelay.delay(8);
		int toSpawn = npc.getPosition().getRegion().players.size();
		for (int i = 0; i < toSpawn; i++) {
			BloodNylo bloodNylo = new BloodNylo(npc, getAbsolute(Random.get(45, 48), Random.get(18, 22)));
			bloodNylos.add(bloodNylo);
		}
		for (int i = 0; i < toSpawn; i++) {
			BloodNylo bloodNylo = new BloodNylo(npc, getAbsolute(Random.get(45, 48), Random.get(40, 43)));
			bloodNylos.add(bloodNylo);
		}
		stage++;
	}

	public Position getAbsolute(int localX, int localY) {
		return new Position(npc.getPosition().getRegion().baseX + localX, npc.getPosition().getRegion().baseY + localY, npc.getPosition().getZ());
	}

	int maxHit = 36;

	private void tornadoAttack(Entity target, NPC npc) {
		if (target.getPosition().isWithinDistance(npc.getPosition(), 20)) {
			AtomicInteger maxDamage = new AtomicInteger(maxHit);
			npc.animate(TORNADO_ANIM);
			Position pos = target.getPosition().copy();
			npc.addEvent(event -> {
				int delay = tornadoProjectile.send(npc, target);
				event.delay(3);
				if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
					maxDamage.set(11);
				else perfectMaidenFailed = true;
				Hit hit = new Hit(npc, AttackStyle.MAGIC).randDamage(maxDamage.get()).ignorePrayer().ignoreDefence().clientDelay(delay);
				target.hit(hit);
			});
		}
	}

	private MaidenNPC asMaiden() {
		return (MaidenNPC) npc;
	}
}
