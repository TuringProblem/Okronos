package io.ruin.model.activities.raids.toa.bosses.kephri;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.activities.raids.toa.Invocations;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.route.routes.DumbRoute;
import io.ruin.utility.TickDelay;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Kephri extends NPCCombat {

	private static final int AWAKENED = 1494;

	boolean canAttack = true;

	private boolean invocationsSet = false;
	List<Invocations> raidInvocations = new ArrayList<>();
	private static final Projectile BOMB_PROJECTILE = new Projectile(2266, 210, 27, 35, 20, 20, 16, 192);
	private static final Projectile BUG_PROJECTILE = new Projectile(2147, 140, 0, 35, 100, 20, 16, 192);
	private static final Projectile EGG_PROJECTILE = new Projectile(2164, 50, 0, 35, 40, 6, 16, 192);
	private static final Projectile EGG_PROJECTILE2 = new Projectile(2165, 50, 0, 35, 40, 6, 16, 192);

	List<Position> swarmSpawnpoints = new ArrayList<>();
	@Getter
	List<Swarm> swarms = new ArrayList<>();

	Position meleeSpawn;
	Position rangeSpawn;
	Position mageSpawn;
	int swarmIndex = 0;

	public boolean perfectKephriFailed = false;
	boolean failedAllOutOfMedics = false;

	int phase = 0;

	boolean inRecovery = false;
	private Bounds[] BOSS_AREA;

	@Override
	public boolean allowRespawn() {
		return false;
	}

	@Override
	public void init() {
		npc.hitListener = new HitListener().postDamage(this::postDamage).preDefend(this::preHitDefend);
		invocationsSet = false;
		inRecovery = false;
		phase = 0;
		swarmIndex = 0;
		swarms.clear();
		swarmSpawnpoints.clear();
		setSwarmSpawnpoints();
		meleeSpawn = new Position(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 32, 0);
		rangeSpawn = new Position(npc.getPosition().getRegion().baseX + 25, npc.getPosition().getRegion().baseY + 33, 0);
		mageSpawn = new Position(npc.getPosition().getRegion().baseX + 38, npc.getPosition().getRegion().baseY + 25, 0);
		BOSS_AREA = new Bounds[] {
				new Bounds(new Position(npc.getPosition().getRegion().baseX + 23, npc.getPosition().getRegion().baseY + 24, 0),
						new Position(npc.getPosition().getRegion().baseX + 28, npc.getPosition().getRegion().baseY + 40, 0), 0),
				new Bounds(new Position(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 35, 0),
						new Position(npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 40, 0), 0),
				new Bounds(new Position(npc.getPosition().getRegion().baseX + 34, npc.getPosition().getRegion().baseY + 24, 0),
						new Position(npc.getPosition().getRegion().baseX + 39, npc.getPosition().getRegion().baseY + 40, 0), 0),
				new Bounds(new Position(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 24, 0),
						new Position(npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 29, 0), 0)

		};
	}

	private void postDamage(Hit hit) {

	}

	private boolean inBossBounds(Player player) {
		for (Bounds bounds : BOSS_AREA) {
			if (player.getPosition().inBounds(bounds)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void startDeath(Hit killHit) {
		if (phase < 3) {
			npc.setHp(30);
			if (inRecovery) {
				return;
			}
			inRecovery = true;
			canAttack = false;
			npc.getPosition().getRegion().players.forEach(p -> p.getCombat().reset());
			npc.getCombat().reset();
			npc.queuedHits.clear();
			restore();
			attacksSinceBombs = 0;
			attacksTilPoop = 5;
			npc.setHp(30);
			npc.animate(9579);
			startRecoveryPhase();
		} else {
			finishRoomEvent();
		}
	}

	private static String format(long millis) {
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes);
		return String.format("%02d:%02d", minutes, seconds);
	}

	boolean dead = false;

	private void finishRoomEvent() {
		dead = true;

		var localPlayers = npc.localPlayers();
		var regionPlayers = npc.getPosition().getRegion().players;

		Player player = localPlayers.isEmpty() ? null : localPlayers.getFirst();
		if (player == null) {
			player = regionPlayers.isEmpty() ? null : regionPlayers.getFirst();
		}

		if (player != null) {
			player.getCurrentToARaid().teleportDeadPlayers();
		}
		long tumekenTime = timer.stop(player, -1);
		regionPlayers.forEach(p -> {
			if (p != null) {
				if (!failedAllOutOfMedics && p.getCurrentToARaid() != null
						&& !p.getCurrentToARaid().deadMembers.contains(p.getName())) {
					if (raidInvocations.contains(Invocations.MEDIC)) {
						Objects.requireNonNull(p.combatAchievementsList
								.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.ALL_OUT_OF_MEDICS.ordinal()))
								.getCombatAchievement()).check(p);
					}
				}
				if (!perfectKephriFailed && p.getCurrentToARaid() != null
						&& !p.getCurrentToARaid().deadMembers.contains(p.getName())
						&& p.getCurrentToARaid().currentParty.getMembers().size() > 1) {
					if (raidInvocations.contains(Invocations.MEDIC) && raidInvocations.contains(Invocations.LIVELY_LARVAE) &&
							raidInvocations.contains(Invocations.MORE_OVERLORDS)
							&& raidInvocations.contains(Invocations.AERIAL_ASSAULT)) {
						Objects.requireNonNull(p.combatAchievementsList
								.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_KEPHRI.ordinal()))
								.getCombatAchievement()).check(p);
						if (p.getCurrentToARaid() != null) {
							p.getCurrentToARaid().perfectKephri = true;
						}
					}
				}
				if (p.getCurrentToARaid() != null && p.getCurrentToARaid().getKephriPathLevel() >= 4
						&& !p.getCurrentToARaid().deadMembers.contains(p.getName())) {
					if (raidInvocations.contains(Invocations.MEDIC) && raidInvocations.contains(Invocations.LIVELY_LARVAE) &&
							raidInvocations.contains(Invocations.MORE_OVERLORDS)
							&& raidInvocations.contains(Invocations.AERIAL_ASSAULT)) {
						Objects.requireNonNull(p.combatAchievementsList
								.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.DOESNT_BUG_ME.ordinal()))
								.getCombatAchievement()).check(p);
					}
				}
				npc.remove();
				p.getStats().restore(false);
				p.cureVenom(0);
				p.getCombat().restoreSpecial(100);
				p.getCombat().reset();
				if (p.kephriBestTime == -1) {
					p.sendMessage("Duration: <col=ef1020>" + format(tumekenTime));
					p.kephriBestTime = tumekenTime;
					return;
				} else if (p.kephriBestTime == 0 || tumekenTime < p.kephriBestTime) {
					p.sendMessage("Duration: <col=ef1020>" + format(tumekenTime) + "</col> (new personal best)");
					p.kephriBestTime = tumekenTime;
					return;
				} else {
					p.sendMessage(
							"Duration: <col=ef1020>" + format(tumekenTime) + "</col>. Personal best: " + format(p.kephriBestTime));
					return;
				}
			}
		});
		npc.transform(11722);
		npc.getCombat().reset();
		npc.faceNone(false);
		for (int i = poops.size() - 1; i >= 0; i--) {
			poops.get(i).remove();
		}
		for (int i = scarabs.size() - 1; i >= 0; i--) {
			scarabs.get(i).remove();
		}
		NPC osmumten = new NPC(11689).spawn(npc.getPosition().getRegion().baseX + 36,
				npc.getPosition().getRegion().baseY + 32, npc.getHeight());
		osmumten.face(Direction.WEST);
	}

	private void preHitDefend(Hit hit) {
		if (hit.attacker != null && hit.attacker.isPlayer()) {
			if (npc.getId() == 119722) {
				hit.attacker.player.sendMessage("Kephri is currently immune to your attacks!");
				hit.block();
			}
		}
		if (hit.attacker != null) {
			Player hitter = hit.attacker.player;
			if (hitter != null) {
				if (hitter.getCurrentToARaid() != null) {
					hit.boostDefence(hitter.getCurrentToARaid().getKephriPathLevel() * 1.1);
				}
			}
		}
	}

	private void startRecoveryPhase() {
		World.startEvent(event -> {
			event.setCancelCondition(() -> npc.getCombat().isDead() || npc.isRemoved());
			event.delay(2);
			npc.transform(11722);
			spawnMinions();
			phase++;

			for (int i = 0; i < 18; i++) {
				event.delay(4);
				sendSwarm();
				if (i == 17) {
					npc.animate(9581);
					event.delay(2);
					npc.transform(11719);
					event.delay(4);
					medicSwarmDelay.reset();
					inRecovery = false;
					canAttack = true;
					spawnEggs();
				}
			}
		});
	}

	TickDelay medicSwarmDelay = new TickDelay();

	private void sendMedicSwarm() {
		if (medicSwarmDelay.remaining() > 0) {
			return;
		}
		medicSwarmDelay.delay(35);
		World.startEvent(e -> {
			e.setCancelCondition(() -> npc.getCombat().isDead() || npc.isRemoved());
			for (int i = 0; i < 15; i++) {
				e.delay(9);
				sendSwarm();
			}
		});
	}

	@Override
	public void follow() {

	}

	int attacksSinceEggs = 0;
	int attacksTilEggs = 11;
	int attacksSincePoop = 0;
	int attacksTilPoop = 5;

	@Override
	public boolean attack() {
		if (npc.getId() == 11722)
			return true;
		if (npc.getId() != 11722) {
			attacksSinceEggs++;
			if (attacksSinceEggs >= attacksTilEggs) {
				attacksSinceEggs = 0;
				attacksTilEggs = Random.get(5, 12);
				spawnEggs();
				return true;
			}
			attacksSincePoop++;
			if (attacksSincePoop >= attacksTilPoop) {
				attacksSincePoop = 0;
				attacksTilPoop = Random.get(8, 11);
				canAttack = false;
				throwPoop();
				return true;
			}
		}
		if (Random.get(10) == 0 && raidInvocations.contains(Invocations.MEDIC)) {
			sendMedicSwarm();
		}
		int patternType;
		if (raidInvocations.contains(Invocations.AERIAL_ASSAULT))
			patternType = 2;
		else {
			patternType = 1;
		}
		int attacksTilBomb = 5;
		npc.getCombat().getInfo().attack_ticks = attacksTilBomb;
		if (canAttack || npc.getId() == 11722) {
			npc.getPosition().getRegion().players.forEach(p -> sendBomb(p, patternType));
			if (npc.getId() == 11719)
				npc.animate(9577);
		}
		return true;
	}

	private int getClosestX(Entity target) {
		if (target.getAbsX() < npc.getAbsX())
			return npc.getAbsX();
		else if (target.getAbsX() >= npc.getAbsX() && target.getAbsX() <= npc.getAbsX() + npc.getSize() - 1)
			return target.getAbsX();
		else
			return npc.getAbsX() + npc.getSize() - 1;
	}

	private int getClosestY(Entity target) {
		if (target.getAbsY() < npc.getAbsY())
			return npc.getAbsY();
		else if (target.getAbsY() >= npc.getAbsY() && target.getAbsY() <= npc.getAbsY() + npc.getSize() - 1)
			return target.getAbsY();
		else
			return npc.getAbsY() + npc.getSize() - 1;
	}

	private void knockback(Player target) {
		if (target == null)
			return;
		int vecX = (target.getAbsX() - getClosestX(target));
		if (vecX != 0)
			vecX /= Math.abs(vecX); // determines X component for knockback
		int vecY = (target.getAbsY() - getClosestY(target));
		if (vecY != 0)
			vecY /= Math.abs(vecY); // determines Y component for knockback
		int endX = target.getAbsX();
		int endY = target.getAbsY();
		for (int i = 0; i < 4; i++) {
			if (DumbRoute.getDirection(endX, endY, npc.getHeight(), target.getSize(), endX + vecX, endY + vecY) != null) {
				endX += vecX;
				endY += vecY;
			} else
				break; // cant take the step, stop here
		}
		if (endX != target.getAbsX() || endY != target.getAbsY()) {
			if (target.player != null) {
				final Player p = target.player;
				p.lock();
				p.animate(9799);
				p.graphics(245, 5, 124);
				p.stun(2, true);
				p.stepAbs(endX, endY, StepType.RUN);
				p.unlock();
			}
		} else {
			target.animate(9799);
			target.graphics(245, 5, 124);
			target.stun(2, true);
		}
	}

	TickDelay poopDelay = new TickDelay();

	private void throwPoop() {
		if (poopDelay.remaining() > 0) {
			return;
		}
		poopDelay.delaySeconds(30);
		if (npc.getId() == 11722)
			return;
		World.startEvent(e -> {
			e.setCancelCondition(() -> npc.getId() == 11722 || npc.getCombat().isDead() || npc.isRemoved());
			List<Player> potentialTargets = npc.getPosition().getRegion().players.stream().filter(this::inBossBounds).toList();
			Player p = Random.get(potentialTargets);
			List<Player> playersBeingHit = new ArrayList<>();
			playersBeingHit.add(p);
			if (raidInvocations.contains(Invocations.BLOWING_MUD) && !potentialTargets.isEmpty())
				playersBeingHit.add(Random.get(potentialTargets));
			playersBeingHit.forEach(plr -> {
				World.sendGraphics(2146, 0, 0, plr.getPosition());
				e.delay(5);
				npc.animate(9578);
				knockback(plr);
				Position target = plr.getPosition().copy();
				Position boss = npc.getCentrePosition().copy();

				List<Position> linePositions = getLinePositions(boss, target);
				placePoop(linePositions);
			});
		});
	}

	List<GameObject> poops = new ArrayList<>();
	List<NPC> scarabs = new ArrayList<>();

	private void placePoop(List<Position> positions) {
		World.startEvent(e -> {
			e.setCancelCondition(() -> npc.getId() == 11722 || npc.getCombat().isDead() || npc.isRemoved());
			e.delay(6);
			canAttack = true;
		});
		int i = 0;
		for (Position position : positions) {
			boolean inArea = false;
			for (Bounds bounds : BOSS_AREA) {
				if (position.inBounds(bounds)) {
					inArea = true;
					break;
				}
			}
			if (inArea) {
				i++;
				World.sendGraphics(2145, 0, i * 5, position);
				int delay = i * 5;
				World.startEvent(event -> {
					event.setCancelCondition(() -> npc.getId() == 11722 || npc.getCombat().isDead() || npc.isRemoved());
					event.delay(World.getTicks(delay));
					GameObject poop = new GameObject(45504, position.getX(), position.getY(), position.getZ(), 10, 0).spawn();
					poops.add(poop);
				});
			}
		}
	}

	private List<Position> getLinePositions(Position start, Position target) {
		List<Position> positions = new ArrayList<>();

		int x1 = start.getX();
		int y1 = start.getY();
		int x2 = target.getX();
		int y2 = target.getY();

		int dx = Math.abs(x2 - x1);
		int dy = Math.abs(y2 - y1);

		int sx = (x1 < x2) ? 1 : -1;
		int sy = (y1 < y2) ? 1 : -1;

		int err = dx - dy;

		int loops = 0;

		while (true) {
			Position currentPosition = new Position(x1, y1);
			positions.add(currentPosition);

			// Move to the next position
			int e2 = 2 * err;
			if (e2 > -dy) {
				err -= dy;
				x1 += sx; // Move horizontally
			}
			if (e2 < dx) {
				err += dx;
				y1 += sy; // Move vertically
			}

			// Check if the next position is within any of the boss area bounds
			boolean withinBounds = false;
			for (Bounds bounds : BOSS_AREA) {
				if (new Position(x1, y1).inBounds(bounds)) {
					withinBounds = true;
					break;
				}
			}
			loops++;

			// Exit loop if the next position goes out of bounds
			if (!withinBounds && loops > 3) {
				break;
			}
		}

		return positions;
	}

	private void spawnAgileEgg(int delay, Position spawn) {
		World.startEvent(e -> {
			e.setCancelCondition(() -> npc.getId() == 11722 || npc.getCombat().isDead() || npc.isRemoved());
			e.delay(delay);
			NPC egg = new AgileScarabEgg(11729, npc).spawn(spawn);
			egg.getCombat().setAllowRetaliate(false);
			scarabs.add(egg);
		});
	}

	private void spawnExplosionEgg(int delay, Position spawnPoint) {
		var bombPositions = new ArrayList<Position>();
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				bombPositions.add(Position.of(spawnPoint.getX() + x, spawnPoint.getY() + y, spawnPoint.getZ()));
			}
		}
		var egg = new NPC(11728).spawn(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ());
			egg.getCombat().setAllowRetaliate(false);
		scarabs.add(egg);

		bombPositions.forEach(pos -> {
			World.startEvent(e -> {
				e.setCancelCondition(() -> npc.getCombat().isDead() || npc.isRemoved());
				e.delay(delay);
				e.delay(30);
				World.sendGraphics(2156, 0, 0, pos);
				npc.getPosition().getRegion().players.forEach(p -> {
					if (p.getPosition().distance(egg.getPosition()) < 2) {
						p.hit(new Hit(npc).fixedDamage(15).ignoreDefence().ignorePrayer());
					}
				});
				egg.remove();
			});
		});

	}

	TickDelay eggDelay = new TickDelay();

	private void spawnEggs() {
		if (eggDelay.remaining() > 0) {
			return;
		}
		eggDelay.delay(30);
		npc.animate(9578);
		int eggsToSpawn = 7;
		if (raidInvocations.contains(Invocations.LIVELY_LARVAE))
			eggsToSpawn = 11;
		for (int i = 0; i < eggsToSpawn; i++) {
			if (Random.get(2) == 0) {
				Position spawn = BOSS_AREA[Random.get(BOSS_AREA.length - 1)].randomPosition();
				int delay = EGG_PROJECTILE2.send(npc, spawn);
				spawnAgileEgg(delay, spawn);
			}
			else {
				Position spawnPoint = getRandomPosition();
				int delay = EGG_PROJECTILE.send(npc, spawnPoint);
				spawnExplosionEgg(World.getTicks(delay), spawnPoint);
			}
		}
	}

	private Position getRandomPosition() {
		return BOSS_AREA[Random.get(BOSS_AREA.length - 1)].randomPosition();
	}

	private Player getNearestPlayerFromPosition(Position position) {
		Player nearest = null;
		int distance = Integer.MAX_VALUE;
		for (Player player : npc.localPlayers()) {
			if (player == null || player.getPosition().getZ() != position.getZ()) {
				continue;
			}
			int dist = player.getPosition().distance(position);
			if (dist < distance) {
				nearest = player;
				distance = dist;
			}
		}
		return nearest;
	}

	int minionsSpawned = 0;

	private void spawnMinions() {
		boolean sendAllThree = false;
		Player player = Random.get(npc.getPosition().getRegion().players);
		if (player != null && player.getCurrentToARaid() != null) {
			if (Random.get(1) == 0 && player.getCurrentToARaid().getKephriPathLevel() >= 2) {
				sendAllThree = true;
			}
		}
		if (raidInvocations.contains(Invocations.MORE_OVERLORDS) || sendAllThree) {
			NPC scarabMager = new NPC(11726).spawn(mageSpawn);
			scarabMager.getCombat().setTarget(getNearestPlayerFromPosition(mageSpawn));
			NPC scarabRanger = new NPC(11725).spawn(rangeSpawn);
			scarabRanger.getCombat().setTarget(getNearestPlayerFromPosition(rangeSpawn));
			NPC scarabSoldier = new NPC(11724).spawn(meleeSpawn);
			scarabSoldier.getCombat().setTarget(getNearestPlayerFromPosition(meleeSpawn));
			scarabs.add(scarabMager);
			scarabs.add(scarabRanger);
			scarabs.add(scarabSoldier);
		} else if (minionsSpawned == 0) {
			NPC scarabMager = new NPC(11726).spawn(mageSpawn);
			scarabMager.getCombat().setTarget(getNearestPlayerFromPosition(mageSpawn));
			NPC scarabRanger = new NPC(11725).spawn(rangeSpawn);
			scarabRanger.getCombat().setTarget(getNearestPlayerFromPosition(rangeSpawn));
			scarabs.add(scarabMager);
			scarabs.add(scarabRanger);
		} else {
			NPC scarabMager = new NPC(11726).spawn(mageSpawn);
			scarabMager.getCombat().setTarget(getNearestPlayerFromPosition(mageSpawn));
			NPC scarabSoldier = new NPC(11724).spawn(meleeSpawn);
			scarabSoldier.getCombat().setTarget(getNearestPlayerFromPosition(meleeSpawn));
			scarabs.add(scarabMager);
			scarabs.add(scarabSoldier);
		}
		minionsSpawned++;
	}

	private void sendBomb(Player target, int patternType) {
		if (!inBossBounds(target))
			return;
		Position targetPos = target.getPosition().copy();
		List<Position> bombPositions = new ArrayList<>();
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				bombPositions.add(new Position(targetPos.getX() + x, targetPos.getY() + y, targetPos.getZ()));
			}
		}
		Projectile bug = BUG_PROJECTILE;
		var cornerLocation = getNearestCorner(targetPos);
		if (cornerLocation.distance(targetPos) < 5)
			bug.setDurationIncrement(10);
		else
			bug.setDurationIncrement(6);
		if (patternType == 1) {
			bombPositions.forEach(pos -> {
				World.startEvent(e -> {
					e.setCancelCondition(() -> npc.getCombat().isDead() || npc.isRemoved());
					World.sendGraphics(1447, 0, 0, pos);
					e.delay(inRecovery ? 3 : 2);
					World.sendGraphics(1447, 0, 0, pos);
					int delay = inRecovery ? bug.send(cornerLocation, targetPos) : BOMB_PROJECTILE.send(npc, targetPos);
					int ticks = World.getTicks(delay) + 1;
					e.delay(ticks);
					World.sendGraphics(2156, 0, 0, pos);
					npc.getPosition().getRegion().players.forEach(p -> {
						if (p.getPosition().distance(pos) < 1) {
							perfectKephriFailed = true;
							p.hit(new Hit(npc).fixedDamage(30).ignoreDefence().ignorePrayer());
						}
					});
				});
			});
		}
		if (patternType == 2) {
			bombPositions.forEach(pos -> {
				World.startEvent(e -> {
					e.setCancelCondition(() -> npc.getId() == 11722 || npc.getCombat().isDead() || npc.isRemoved());
					World.sendGraphics(1447, 0, 0, pos);
					e.delay(2);
					World.sendGraphics(1447, 0, 0, pos);
					int delay = inRecovery ? bug.send(cornerLocation, targetPos) : BOMB_PROJECTILE.send(npc, targetPos);
					int ticks = World.getTicks(delay) + 1;
					World.sendGraphics(2156, 0, delay, pos);
					e.delay(ticks);
					npc.getPosition().getRegion().players.forEach(p -> {
						if (p.getPosition().distance(pos) < 1) {
							perfectKephriFailed = true;
							p.hit(new Hit(npc).fixedDamage(30).ignoreDefence().ignorePrayer());
						}
					});
				});
			});
		}
	}

	private Position getNearestCorner(Position pos) {
		var region = npc.getPosition().getRegion();

		var northWest = Position.of(region.baseX + 24, region.baseY + 39, 0);
		var northEast = Position.of(region.baseX + 38, region.baseY + 39, 0);
		var southEast = Position.of(region.baseX + 38, region.baseY + 25, 0);
		var southWest = Position.of(region.baseX + 24, region.baseY + 25, 0);

		int northWestDist = pos.distance(northWest);
		int northEastDist = pos.distance(northEast);
		int southEastDist = pos.distance(southEast);
		int southWestDist = pos.distance(southWest);

		// Check if any two distances are equal
		if (northWestDist == northEastDist || northWestDist == southEastDist || northWestDist == southWestDist) {
			return northWest; // Arbitrarily choose one corner
		}
		if (northEastDist == southEastDist || northEastDist == southWestDist) {
			return northEast;
		}
		if (southEastDist == southWestDist) {
			return southEast;
		}

		// Otherwise, return the nearest corner
		if (northWestDist < northEastDist && northWestDist < southEastDist && northWestDist < southWestDist) {
			return northWest;
		}
		if (northEastDist < northWestDist && northEastDist < southEastDist && northEastDist < southWestDist) {
			return northEast;
		}
		if (southEastDist < northWestDist && southEastDist < northEastDist && southEastDist < southWestDist) {
			return southEast;
		}
		if (southWestDist < northEastDist && southWestDist < southEastDist) {
			return southWest;
		}
		return southWest;
	}

	private void setSwarmSpawnpoints() {
		swarmSpawnpoints
				.add(new Position(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 39, 0));
		swarmSpawnpoints
				.add(new Position(npc.getPosition().getRegion().baseX + 38, npc.getPosition().getRegion().baseY + 38, 0));
		swarmSpawnpoints
				.add(new Position(npc.getPosition().getRegion().baseX + 38, npc.getPosition().getRegion().baseY + 26, 0));
		swarmSpawnpoints
				.add(new Position(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 25, 0));
		swarmSpawnpoints
				.add(new Position(npc.getPosition().getRegion().baseX + 24, npc.getPosition().getRegion().baseY + 25, 0));
		swarmSpawnpoints
				.add(new Position(npc.getPosition().getRegion().baseX + 24, npc.getPosition().getRegion().baseY + 32, 0));
		swarmSpawnpoints
				.add(new Position(npc.getPosition().getRegion().baseX + 24, npc.getPosition().getRegion().baseY + 39, 0));
	}

	private void sendSwarm() {
		if (npc.getCombat().isDead()) return;
		Position spawnPosition = swarmSpawnpoints.get(swarmIndex);
		swarmIndex++;
		if (swarmIndex >= swarmSpawnpoints.size()) {
			swarmIndex = 0;
		}
		Swarm swarm = (Swarm) new Swarm(11723).spawn(spawnPosition);
		swarms.add(swarm);
		swarm.getCombat().setAllowRetaliate(false);
		swarm.animate(9605);
		World.startEvent(event -> {
			event.setCancelCondition(() -> npc.getCombat().isDead() || npc.isRemoved());
			event.delay(2);
			swarm.moveTowardsKephri(npc);
		});

	}

	int attacksSinceBombs = 0;
	public ActivityTimer timer;
	boolean timerSet = false;

	public int getHpPercentage() {
		return (npc.getHp() * 100) / npc.getMaxHp();
	}

	@Override
	public void process() {
		Player player = npc.getPosition().getRegion().players.isEmpty() ? null : npc.getPosition().getRegion().players.getFirst();
		if (!npc.getPosition().getRegion().players.isEmpty() && player != null && Objects.nonNull(player.getCurrentToARaid())) {
			if (!timerSet) {
				timer = new ActivityTimer();
				timerSet = true;
			}
			if (!invocationsSet) {
				invocationsSet = true;
				raidInvocations = player.getCurrentToARaid().getInvocations();
			}
		}
		if (phase >= 1 && getHpPercentage() > 25)
			failedAllOutOfMedics = true;
		if (!npc.getPosition().getRegion().players.isEmpty()) {
			if (npc.getId() == 11722 && !dead) {
				int attacksTilBomb = 5;
				attacksSinceBombs++;
				if (attacksSinceBombs >= attacksTilBomb) {
					attacksSinceBombs = 0;
					int patternType;
					if (raidInvocations.contains(Invocations.AERIAL_ASSAULT))
						patternType = 2;
					else {
						patternType = 1;
					}
					npc.getPosition().getRegion().players.forEach(p -> sendBomb(p, patternType));
				}
			}
		}
		for (NPC scarab : scarabs) {
			if (scarab.getCombat() instanceof ScarabRanger ranger) {
				if (ranger.damagedPlayer)
					perfectKephriFailed = true;
				for (int i = swarms.size() - 1; i >= 0; i--) {
					Swarm swarm = swarms.get(i);
					if (swarm.getPosition().distance(npc.getPosition()) < 2 && swarm.getHp() > 0) {
						swarm.remove();
						npc.hit(new Hit(HitType.HEAL).fixedDamage(30));
						swarms.remove(i);
					}
				}
			}
		}
	}
}
