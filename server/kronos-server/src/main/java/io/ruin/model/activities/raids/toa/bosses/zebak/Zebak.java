package io.ruin.model.activities.raids.toa.bosses.zebak;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.activities.raids.toa.Invocations;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.*;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.TickDelay;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class Zebak extends NPCCombat {
	Position wavePosition;
	Bounds bossBounds;
	Bounds bossAttackBounds;
	Bounds rockSpawnBounds;
	Bounds bloodSpawnSpawnBounds;

	int attacksUntilDangerTileActivates = 8;
	int lastSpecialAttack = -1;

	int specialsActivated = 0;

	public boolean perfectZebak = true;

	@Override
	public void init() {
		npc.hitListener = new HitListener().postDamage(this::postDamage).preDefend(this::preHitDefend);
		wavePosition = new Position(npc.getPosition().getRegion().baseX + 38, npc.getPosition().getRegion().baseY + 23,
				npc.getPosition().getZ());
		bossBounds = new Bounds(npc.getPosition().getRegion().baseX + 20, npc.getPosition().getRegion().baseY + 21,
				npc.getPosition().getRegion().baseX + 41, npc.getPosition().getRegion().baseY + 42, 0);
		bossAttackBounds = new Bounds(npc.getPosition().getRegion().baseX + 18, npc.getPosition().getRegion().baseY + 20,
				npc.getPosition().getRegion().baseX + 42, npc.getPosition().getRegion().baseY + 46, 0);
		rockSpawnBounds = new Bounds(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 27,
				npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 37, 0);
		bloodSpawnSpawnBounds =
				new Bounds(npc.getPosition().getRegion().baseX + 25, npc.getPosition().getRegion().baseY + 27,
						npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 38, 0);
	}

	private void preHitDefend(Hit hit) {
		if (hit.attacker != null) {
			Player hitter = hit.attacker.player;
			if (hitter != null) {
				if (hitter.getCurrentToARaid() != null) {
					hit.boostDefence(hitter.getCurrentToARaid().getZebakPathLevel() * 1.1);
				}
			}
		}
	}

	private boolean attackedWithNonMelee = false;

	private void postDamage(Hit hit) {
		if (!rockSpecialActive && !wavesActive) {
			if (getHpPercentage() <= 85 && specialsActivated == 0)
				sendSpecial();
			else if (getHpPercentage() <= 70 && specialsActivated == 1 && !wavesActive)
				sendSpecial();
			else if (getHpPercentage() <= 55 && specialsActivated == 2 && !rockSpecialActive)
				sendSpecial();
			else if (getHpPercentage() <= 40 && specialsActivated == 3 && !wavesActive)
				sendSpecial();

			if (hit.attackStyle != null && !hit.attackStyle.isMelee())
				attackedWithNonMelee = true;
		}
	}

	private void meleeAttack() {
		npc.animate(9770);
		Player player = (Player) target;
		int prayerDamage = 12;
		if (player != null && player.getCurrentToARaid() != null
				&& player.getCurrentToARaid().getInvocations().contains(Invocations.QUIET_PRAYERS))
			prayerDamage = 15;

		if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE))
			target.hit(new Hit(npc).randDamage(prayerDamage));
		else {
			target.hit(new Hit(npc).randDamage(42));
			perfectZebak = false;
		}
	}


	private void sendSpecial() {
		specialsActivated++;
		if (lastSpecialAttack == -1) {
			if (Random.get(1) == 0)
				waveSpecial();
			else
				spawnCoverRocks();
		} else if (lastSpecialAttack == 1)
			spawnCoverRocks();
		else
			waveSpecial();
	}

	private int getHpPercentage() {
		return (npc.getHp() * 100) / npc.getMaxHp();
	}

	@Override
	public void follow() {

	}

	boolean dangerFloorActive = false;

	int attackSpeed = 5;

	@Override
	public boolean attack() {
		return false;

	}



	private void sendRandomAcid() {
		npc.animate(9632);
		List<Position> acidPositions = new ArrayList<>();


		Bounds bounds = new Bounds(getMiddleOfRoom().getX() - 1, getMiddleOfRoom().getY() - 2, getMiddleOfRoom().getX() + 2,
				getMiddleOfRoom().getY() + 1, getMiddleOfRoom().getZ());
		if (invocations.contains(Invocations.UPSET_STOMACH))
			bounds = new Bounds(getMiddleOfRoom().getX() - 2, getMiddleOfRoom().getY() - 3, getMiddleOfRoom().getX() + 3,
					getMiddleOfRoom().getY() + 2, getMiddleOfRoom().getZ());
		bounds.forEachPos(pos -> {
			if (Random.get(3) != 0)
				acidPositions.add(pos);
		});
		for (int i = 0; i < 12; i++) {
			Position pos = bossBounds.randomPositionWithClipping();
			if (pos == null)
				continue;
			if (!acidPositions.contains(pos))
				acidPositions.add(pos);
		}
		List<Integer> acidIds = new ArrayList<>();
		for (int i = 45571; i < 45577; i++)
			acidIds.add(i);
		for (Position acidPosition : acidPositions) {
			World.startEvent(e -> {
				int delay = acidProjectile.send(npc, acidPosition); // Send acid projectile
				e.delay(World.getTicks(delay) + 1); // Delay
				GameObject acid =
						new GameObject(Random.get(acidIds), acidPosition.getX(), acidPosition.getY(), acidPosition.getZ(), 10, 0)
								.spawn(); // Spawn acid object
				acidGameObjects.add(acid);
			});
		}
	}

	private List<Position> generateBulkPatchPositions(Position centerPosition, int patchSize) {
		List<Position> bulkPatchPositions = new ArrayList<>();
		int halfSize = patchSize / 2;

		for (int x = -halfSize; x <= halfSize; x++) {
			for (int y = -halfSize; y <= halfSize; y++) {
				Position newPosition =
						new Position(centerPosition.getX() + x, centerPosition.getY() + y, centerPosition.getZ());
				bulkPatchPositions.add(newPosition);

			}
		}
		return bulkPatchPositions;
	}

	@Override
	public int getAggressionRange() {
		return 40;
	}

	@Override
	public int getAttackBoundsRange() {
		return 40;
	}

	private void handleSwitchAttackStyle() {
		if (currentAttackStyle == AttackStyle.MAGIC) {
			currentAttackStyle = AttackStyle.RANGED;
		} else {
			currentAttackStyle = AttackStyle.MAGIC;
		}
	}

	AttackStyle currentAttackStyle = AttackStyle.MAGIC;

	Projectile rangedStartProjectile = new Projectile(2178, 65, 90, 20, 15, 14, 15, 10).regionBased();
	Projectile mageStartProjectile = new Projectile(2176, 65, 90, 20, 15, 14, 15, 10).regionBased();
	List<Invocations> invocations = new ArrayList<>();

	private void handleAutoAttack() {
		// todo: projectile speed based on dist
		npc.animate(9624);
		if (Random.get(10) == 0 && invocations.contains(Invocations.NOT_JUST_A_HEAD))
			spawnBloodSpawns();
		AtomicInteger maxDamage = new AtomicInteger(60);
		if (Random.get(10) == 0) {
			handleSwitchAttackStyle();
		}
		World.startEvent(e -> {
			e.delay(2);
			if (!npc.getPosition().getRegion().players.isEmpty()) {
				for (int i = npc.getPosition().getRegion().players.size() - 1; i >= 0; i--) {
					if (i >= npc.getPosition().getRegion().players.size()) {
						break;
					}

					Player player = npc.getPosition().getRegion().players.get(i);
					if (!player.getPosition().inBounds(bossAttackBounds)) {
						continue;
					}

					Position startPosition =
							new Position(npc.getPosition().getX() + 9, npc.getPosition().getY() + 5, npc.getPosition().getZ());
					int distance = startPosition.distance(player.getPosition());
					int projectileSpeed = 37;
					if (distance < 4) {
						projectileSpeed = 59;
						startPosition =
								new Position(npc.getPosition().getX() + 7, npc.getPosition().getY() + 5, npc.getPosition().getZ());
					}
					Projectile rangedProjectile = new Projectile(2187, 90, 30, 20, 15, projectileSpeed, 15, 10).regionBased();
					Projectile magicProjectile = new Projectile(2181, 90, 30, 20, 15, projectileSpeed, 15, 10).regionBased();

					boolean magic = false;
					if (currentAttackStyle == AttackStyle.MAGIC)
						magic = true;
					int delay2 = currentAttackStyle == AttackStyle.MAGIC ? magicProjectile.send(startPosition, player)
							: rangedProjectile.send(startPosition, player);
					e.delay(World.getTicks(delay2) + 1);
					int prayerMultiplier;
					if (player != null && player.getCurrentToARaid() != null
							&& player.getCurrentToARaid().getInvocations().contains(Invocations.QUIET_PRAYERS))
						prayerMultiplier = 7;
					else {
						prayerMultiplier = 10;
					}
					if (magic && player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)) {
						maxDamage.updateAndGet(v -> v / prayerMultiplier);
					} else if (!magic && player.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES)) {
						maxDamage.updateAndGet(v -> v / prayerMultiplier);
					} else {
						perfectZebak = false;
					}
					if (player.getPosition().inBounds(bossBounds) && npc.getHp() > 0)
						player.hit(new Hit().randDamage(0, maxDamage.get()).ignorePrayer());
					if (invocations.contains(Invocations.NOT_JUST_A_HEAD) && Random.get(4) == 0)
						bloodBlitz(player);
				}

			}
		});
	}

	private void handleJugExplosion(Jug jug) {
		jug.graphics(2192, 0, 0);
		jugs.remove(jug);
		jug.remove();
		Bounds explosionBounds = new Bounds(jug.getPosition().getX() - 2, jug.getPosition().getY() - 3,
				jug.getPosition().getX() + 3, jug.getPosition().getY() + 2, jug.getPosition().getZ());
		if (invocations.contains(Invocations.UPSET_STOMACH)) {
			explosionBounds = new Bounds(jug.getPosition().getX() - 1, jug.getPosition().getY() - 2,
					jug.getPosition().getX() + 2, jug.getPosition().getY() + 1, jug.getPosition().getZ());

		}
		for (int i = acidGameObjects.size() - 1; i >= 0; i--) {
			GameObject acid = acidGameObjects.get(i);
			if (acid.getPosition().inBounds(explosionBounds)) {
				acidGameObjects.remove(acid);
				acid.remove();
			}
		}
		explosionBounds.forEachPos(pos -> {
			World.sendGraphics(2193, 0, 0, pos);
		});
	}

	private Position getMiddleOfRoom() {
		return new Position(bossBounds.swX + (bossBounds.neX - bossBounds.swX) / 2,
				bossBounds.swY + (bossBounds.neY - bossBounds.swY) / 2, bossBounds.z);
	}

	int attacksTilPoison = 3;
	boolean invocationsSet = false;
	public ActivityTimer timer;
	boolean timerSet = false;

	@Override
	public void process() {
		if (!npc.getPosition().getRegion().players.isEmpty()) {
			if (!timerSet) {
				timer = new ActivityTimer();
				timerSet = true;
			}
			if (!invocationsSet) {
				Player player = npc.getPosition().getRegion().players.getFirst();
				invocationsSet = true;
				invocations = player.getCurrentToARaid().getInvocations();
			}
		}
		int playerInBossBounds = 0;
		for (Player player : npc.getPosition().getRegion().players) {
			if (player.getPosition().inBounds(bossAttackBounds))
				playerInBossBounds++;
		}
		if (playerInBossBounds == 0)
			return;
		if (npc.getId() != 11730)
			return;

		for (int i = rocks.size() - 1; i >= 0; i--) {
			Rock rock = rocks.get(i);
			if (rock.npc.getHp() < 1) {
				rock.npc.remove();
				rocks.remove(rock);
			}
		}
		if (!npc.getPosition().getRegion().players.isEmpty()) {
			if (attackSpeed-- < 1) {
				if (dangerFloorActive) {
					return;
				}
				for (Player player : npc.getPosition().getRegion().players) {
					if (player.getPosition().distance(npc.getPosition()) < 3) {
						meleeAttack();
						return;
					}
				}
				if (rockSpecialActive) {
					if (attacksUntilDangerTileActivates-- <= 0 && !dangerFloorActive) {
						dangerFloorActive = true;
						handleDangerousTiles();
					}
				}

				if (attacksTilPoison-- <= 0 && !rockSpecialActive) {
					attacksTilPoison = 500;
					sendRandomAcid();
					return;
				}
				int newAttackSpeed = 5;
				handleAutoAttack();
				attackSpeed = newAttackSpeed;
			}
		}
		if (!acidGameObjects.isEmpty()) {
			handleAcidPools();
			for (int i = acidGameObjects.size() - 1; i >= 0; i--) {
				GameObject acid = acidGameObjects.get(i);
				if (npc.getHp() < 1)
					acid.remove();
				for (Rock rock : rocks) {
					if (acid.getPosition().distance(rock.getNpc().getPosition()) < 1) {
						acidGameObjects.remove(acid);
						acid.remove();
					}
				}
			}
		}

		if (!waves.isEmpty()) {
			for (int i = waves.size() - 1; i >= 0; i--) {
				Wave wave = waves.get(i);
				if (npc.getHp() < 1)
					wave.remove();
				wave.update(npc);
				for (int j = acidGameObjects.size() - 1; j >= 0; j--) {
					GameObject acid = acidGameObjects.get(j);
					if (acid.getPosition().distance(wave.getPosition()) < 1 && !wave.isRemoved()) {
						acidGameObjects.remove(acid);
						acid.remove();
					}
				}
				if (wave.isRemoved())
					waves.remove(wave);
			}
		} else {
			wavesActive = false;
		}
		if (!jugs.isEmpty()) {
			for (int i = jugs.size() - 1; i >= 0; i--) {
				Jug jug = jugs.get(i);
				for (Position rockSpawn : rockSpawns) {
					if (jug.getPosition().distance(rockSpawn) < 1) {
						handleJugExplosion(jug);
					}
				}
			}
		}
		if (!bloodSpawns.isEmpty()) {
			for (int i = bloodSpawns.size() - 1; i >= 0; i--) {
				BloodSpawn bloodSpawn = bloodSpawns.get(i);
				bloodSpawn.update(npc);
				if (bloodSpawn.isRemoved())
					bloodSpawns.remove(bloodSpawn);
			}
		}
	}

	List<Wave> waves = new ArrayList<>();
	boolean wavesActive = false;
	int ticksTilAcidCheck = 2;

	private void bloodBlitz(Entity target) {
		World.startEvent(event -> {
			event.setCancelCondition(() -> npc.getHp() < 1 || target.getHp() < 1);
			int minimumDamage = 8;
			int maximumDamage = 11;
			if (invocations.contains(Invocations.ARTERIAL_SPRAY)) {
				minimumDamage = 12;
				maximumDamage = 15;
			}
			int damage = Random.get(minimumDamage, maximumDamage);
			int healAmount = (damage * 66) / 100;
			event.delay(2);
			target.graphics(2003);
			if (!target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)) {
				target.hit(new Hit().fixedDamage(damage));
				npc.hit(new Hit(HitType.HEAL).fixedDamage(healAmount));
			}
		});
	}

	private void waveSpecial() {
		wavesActive = true;
		lastSpecialAttack = 1;
		waves.clear();

		// Pre-calculate indices for all 3 waves at once
		List<int[]> waveIndices = new ArrayList<>(3);
		for (int i = 0; i < 3; i++) {
			int index = Random.get(3, 7);
			int index2;
			if (index == 3)
				index2 = 4;
			else if (index == 7)
				index2 = 6;
			else
				index2 = index + 1;

			if (!npc.getPosition().getRegion().players.isEmpty()) {
				Player player = npc.getPosition().getRegion().players.getFirst();
				if (player != null && player.getCurrentToARaid() != null &&
					player.getCurrentToARaid().getZebakPathLevel() >= 2)
					index2 = index;

				waveIndices.add(new int[] {
					index,
					index2
				});
			}
		}

		World.startEvent(e -> {
			e.setCancelCondition(() -> npc.getHp() < 1);

			for (int waveCount = 0; waveCount < 3; waveCount++) {
				int[] indices = waveIndices.get(waveCount);
				int index = indices[0];
				int index2 = indices[1];

				// Create all wave NPCs for this wave at once
				List<Wave> newWaves = new ArrayList<>();
				for (int i = 0; i < 15; i++) {
					if (i == index || i == index2)
						continue;
					Position pos = new Position(wavePosition.getX() - i, wavePosition.getY(), wavePosition.getZ());
					Wave wave = new Wave(11738, pos, npc);
					newWaves.add(wave);
				}

				// Add all waves at once (reduces list modifications)
				waves.addAll(newWaves);
				e.delay(7);

				if (waveCount == 2) {
					attacksTilPoison = 5;
				}
			}
		});
	}

	List<Position> rockSpawns = new ArrayList<>();
	List<Rock> rocks = new ArrayList<>();
	List<Jug> jugs = new ArrayList<>();
	boolean rockSpecialActive = false;

	private void rockEvent(Position pos, int finalI) {
		World.startEvent(e -> {
			int delay = rangedStartProjectile.send(npc, new Position(pos.getX() + finalI, pos.getY(), pos.getZ()));
			Position rockPos = new Position(pos.getX(), pos.getY() + finalI, pos.getZ());
			rockSpawns.add(rockPos);
			e.delay(World.getTicks(delay) + 1);
			Rock rock = new Rock(rockPos, 11737);
			rock.getNpc().getDef().occupyTiles = true;
			rock.npc.getCombat().setAllowRetaliate(false);
			rock.npc.getDef().occupyTiles = true;
			rocks.add(rock);
		});
	}

	private void spawnCoverRocks() {
		if (rockSpecialActive)
			return;
		lastSpecialAttack = 2;
		rockSpecialActive = true;
		Position pos = rockSpawnBounds.randomPositionWithClipping();
		if (pos == null)
			return;
		rocks.clear();
		for (int i = -1; i < 1; i++) {
			int finalI = i;
			rockEvent(pos, finalI);
		}
		if (Random.get(1) == 0) {
			World.startEvent(e -> {
				int delay = rangedStartProjectile.send(npc, new Position(pos.getX(), pos.getY() + 2, pos.getZ()));
				Position rockPos = new Position(pos.getX(), pos.getY() + 2, pos.getZ());
				rockSpawns.add(rockPos);
				e.delay(World.getTicks(delay) + 1);
				Rock rock = new Rock(rockPos, 11737);
				rock.npc.getCombat().setAllowRetaliate(false);
				rock.npc.getDef().occupyTiles = true;
				rocks.add(rock);
			});
		}



		spawnJugs();
		spawnAcid();
	}

	Projectile jugProjectile = new Projectile(2194, 70, 0, 0, 56, 8, 16, 64).regionBased();

	private void spawnJugs() {
		Bounds jugBounds = new Bounds(npc.getPosition().getRegion().baseX + 26, npc.getPosition().getRegion().baseY + 28,
				npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 37, npc.getPosition().getZ());
		for (int i = 0; i < 2; i++) {
			Position jugSpawn = jugBounds.randomPositionWithClipping();
			int delay = jugProjectile.send(npc, jugSpawn);
			World.startEvent(e -> {
				e.delay(World.getTicks(delay) + 1);
				Jug jug = (Jug) new Jug(11735, rockSpawns).spawn(jugSpawn);
				jug.npc.getCombat().setAllowRetaliate(false);
				jug.npc.hitListener = new HitListener().postDamage(hit -> handleJugExplosion(jug));
				// System.out.println("Jug spawned at: " + jugSpawn);
				jugs.add(jug);
			});

		}
	}

	Projectile acidProjectile = new Projectile(1555, 80, 0, 0, 56, 8, 16, 64).regionBased();
	List<GameObject> acidGameObjects = new ArrayList<>();

	private void spawnAcid() {
		List<Position> acidPositions = new ArrayList<>();
		int totalAcidCount = 20;
		List<Integer> acidIds = new ArrayList<>();
		for (int i = 45571; i < 45577; i++)
			acidIds.add(i);

		// Add acid positions in the safe area
		acidPositions.addAll(getSafeArea());

		// Add additional acid positions around the room
		for (int i = 0; i < totalAcidCount - acidPositions.size(); i++) {
			Position randomPosition = bossBounds.randomPositionWithClipping();
			if (randomPosition == null)
				continue;
			acidPositions.add(randomPosition);
		}
		for (Position pos : rockSpawns) {
			Bounds rockBounds = new Bounds(pos.getX() - 1, pos.getY() - 2, pos.getX() + 1, pos.getY() + 2, pos.getZ());
			rockBounds.forEachPos(acidPositions::add);
		}
		acidPositions.removeIf(pos -> rockSpawns.contains(pos));
		World.startEvent(e -> {
			// Create a queue of positions and delays
			Map<Position, Integer> pendingAcid = new HashMap<>();

			// Send all projectiles first
			for (Position pos : acidPositions) {
				int delay = acidProjectile.send(npc, pos);
				pendingAcid.put(pos, World.getTicks(delay) + 1);
			}

			// Group positions by delay value
			Map<Integer, List<Position>> delayGroups = new HashMap<>();
			pendingAcid.forEach((pos, delay) -> delayGroups.computeIfAbsent(delay, k -> new ArrayList<>()).add(pos));

			// Process delay groups in order
			List<Integer> delays = new ArrayList<>(delayGroups.keySet());
			Collections.sort(delays);

			int currentTick = 0;
			for (int delay : delays) {
				// Wait until the next delay point
				if (delay > currentTick) {
					e.delay(delay - currentTick);
					currentTick = delay;
				}

				// Spawn all acid pools for this delay at once
				for (Position pos : delayGroups.get(delay)) {
					GameObject acid = new GameObject(Random.get(acidIds), pos.getX(), pos.getY(), pos.getZ(), 10, 0).spawn();
					acidGameObjects.add(acid);
				}
			}
		});
	}

	private void handleAcidPools() {
		if (ticksTilAcidCheck-- > 0)
			return;
		ticksTilAcidCheck = 2; // Only check every 2 ticks

		// Get player positions once, not for each acid pool
		Map<Position, Player> playerPositions = new HashMap<>();
		for (Player p : npc.getPosition().getRegion().players) {
			playerPositions.put(p.getPosition(), p);
		}

		for (GameObject acid : acidGameObjects) {
			Position acidPos = acid.getPosition();

			// Check players within 1 tile efficiently
			for (Map.Entry<Position, Player> entry : playerPositions.entrySet()) {
				if (entry.getKey().distance(acidPos) < 1) {
					Player p = entry.getValue();
					if (p.poisonDamageDelay.remaining() < 1) {
						p.poisonDamageDelay.delay(2);
						perfectZebak = false;
						p.hit(new Hit(HitType.POISON).randDamage(8, 12));
					}
				}
			}
		}
	}

	private void sendDangerousTiles() {
		for (int rockIndex = rocks.size() - 1; rockIndex >= 0; rockIndex--) {
			Rock rock = rocks.get(rockIndex);
			rock.npc.hit(new Hit().fixedDamage(50));
			if (rock.npc.getHp() <= 0) {
				rock.npc.graphics(2191, 0, 0);
				rocks.remove(rock);
			}
		}
		for (Position pos : getDangerousArea()) {
			World.sendGraphics(2111, 0, 0, pos);
			npc.getPosition().getRegion().players.forEach(p -> {
				if (p.getPosition().distance(pos) < 1) {
					perfectZebak = false;
					p.hit(new Hit().fixedDamage(50));
				}
			});
		}
	}

	private static String format(long millis) {
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes);
		return String.format("%02d:%02d", minutes, seconds);
	}

	@Override
	public void startDeath(Hit killHit) {

		npc.getCombat().reset();
		npc.getCombat().getInfo().aggressive_level = 1;
		npc.getCombat().setTarget(null);
		npc.getCombat().reset();
		player = npc.getPosition().getRegion().players.getFirst();
		long tumekenTime = timer.stop(player, -1);
		player.getCurrentToARaid().teleportDeadPlayers();
		npc.getPosition().getRegion().players.forEach(p -> {
			if (!attackedWithNonMelee) {
				Objects.requireNonNull(p.combatAchievementsList
						.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.CHOMPINGTON.ordinal()))
						.getCombatAchievement()).check(p);
			}
			if (invocations.contains(Invocations.BLOOD_THINNERS) && invocations.contains(Invocations.NOT_JUST_A_HEAD)
					&& invocations.contains(Invocations.UPSET_STOMACH) && invocations.contains(Invocations.ARTERIAL_SPRAY)) {
				if (p.getCurrentToARaid().getZebakPathLevel() >= 4) {
					Objects.requireNonNull(p.combatAchievementsList
							.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.ROCKIN_AROUND_THE_CROC.ordinal()))
							.getCombatAchievement()).check(p);
				}
			}
			if (perfectZebak) {
				if (invocations.contains(Invocations.BLOOD_THINNERS) && invocations.contains(Invocations.NOT_JUST_A_HEAD)
						&& invocations.contains(Invocations.UPSET_STOMACH) && invocations.contains(Invocations.ARTERIAL_SPRAY)) {
					Objects.requireNonNull(p.combatAchievementsList
							.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_ZEBAK.ordinal()))
							.getCombatAchievement()).check(p);
				}
			}
			if (p != null) {
				p.getStats().restore(false);
				p.cureVenom(0);
				p.getCombat().restoreSpecial(100);
				p.getCombat().reset();
				if (p.zebakBestTime == -1) {
					p.sendMessage("Duration: <col=ef1020>" + format(tumekenTime));
					p.zebakBestTime = tumekenTime;
					return;
				} else if (p.zebakBestTime == 0 || tumekenTime < p.zebakBestTime) {
					p.sendMessage("Duration: <col=ef1020>" + format(tumekenTime) + "</col> (new personal best)");
					p.zebakBestTime = tumekenTime;
					return;
				} else {
					p.sendMessage(
							"Duration: <col=ef1020>" + format(tumekenTime) + "</col>. Personal best: " + format(p.zebakBestTime));
					return;
				}
			}
		});
		for (int i = rocks.size() - 1; i >= 0; i--) {
			Rock rock = rocks.get(i);
			rock.npc.remove();
			rocks.remove(rock);
		}
		for (int i = jugs.size() - 1; i >= 0; i--) {
			Jug jug = jugs.get(i);
			jug.remove();
			jugs.remove(jug);
		}
		for (int i = acidGameObjects.size() - 1; i >= 0; i--) {
			GameObject acid = acidGameObjects.get(i);
			acid.remove();
			acidGameObjects.remove(acid);
		}
		for (int i = waves.size() - 1; i >= 0; i--) {
			Wave wave = waves.get(i);
			wave.remove();
			waves.remove(wave);
		}
		for (int i = bloodSpawns.size() - 1; i >= 0; i--) {
			BloodSpawn bloodSpawn = bloodSpawns.get(i);
			bloodSpawn.remove();
			bloodSpawns.remove(bloodSpawn);
		}
		npc.transform(11733);
		NPC osmumten = new NPC(11689).spawn(npc.getPosition().getRegion().baseX + 28,
				npc.getPosition().getRegion().baseY + 34, npc.getHeight());
		osmumten.face(Direction.EAST);

	}

	private void handleDangerousTiles() {
		World.startEvent(e -> {
			e.setCancelCondition(() -> npc.getHp() < 1);
			for (int i = 0; i < 3; i++) {
				if (npc.getHp() < 1)
					break;
				npc.animate(9628);
				int finalI = i;
				e.delay(2);
				sendDangerousTiles();
				e.delay(2);
				if (finalI == 2) {
					dangerFloorActive = false;
					attacksUntilDangerTileActivates = 8;
					rockSpecialActive = false;
					for (int j = jugs.size() - 1; j >= 0; j--) {
						Jug jug = jugs.get(j);
						jug.remove();
						jugs.remove(jug);
					}
				}
			}
		});
	}

	private List<Position> getDangerousArea() {
		List<Position> dangerousArea = new ArrayList<>();
		bossBounds.forEachPos(position -> {
			boolean safe = false;
			for (Rock rock : rocks) {
				if (position.distance(new Position(rock.getNpc().getPosition().getX() + 1, rock.getNpc().getPosition().getY(),
						rock.getNpc().getPosition().getZ())) < 1) {
					safe = true;
					break;
				}
			}
			if (!safe) {
				dangerousArea.add(position);
			}
		});
		return dangerousArea;
	}

	List<Position> getSafeArea() {
		List<Position> safeArea = new ArrayList<>();
		bossBounds.forEachPos(position -> {
			boolean safe = false;
			for (Rock rock : rocks) {
				int distanceToRock = rock.getNpc().getPosition().distance(position);

				if (distanceToRock < 2 && !isOnSideClosestToBoss(position, rock.getNpc())) {
					safe = true;
					break;
				}
			}
			if (safe) {
				safeArea.add(position);
			}
		});
		return safeArea;
	}

	private boolean isOnSideClosestToBoss(Position position, NPC rock) {
		int rockCenterX = rock.getPosition().getX() + 1;
		int rockCenterY = rock.getPosition().getY() + 1;

		return position.getX() == rockCenterX || position.getY() == rockCenterY;
	}


	private Position getJugSpawnPosition(List<Position> rockSpawns, Bounds rockSpawnBounds) {
		Position jugSpawn;
		do {
			jugSpawn = rockSpawnBounds.randomPositionWithClipping();
		} while (!isValidJugSpawnPosition(jugSpawn, rockSpawns));
		return jugSpawn;
	}

	private boolean isValidJugSpawnPosition(Position jugSpawn, List<Position> rockSpawns) {
		for (Position rockSpawn : rockSpawns) {
			if (isValidDistance(jugSpawn, rockSpawn, 3) && isInLineWithRock(jugSpawn, rockSpawn)) {
				return true;
			}
		}
		return false;
	}

	private boolean isValidDistance(Position pos1, Position pos2, int minDistance) {
		return pos1.distance(pos2) >= minDistance;
	}

	private boolean isInLineWithRock(Position jugSpawn, Position rockSpawn) {
		int deltaX = Math.abs(jugSpawn.getX() - rockSpawn.getX());
		int deltaY = Math.abs(jugSpawn.getY() - rockSpawn.getY());

		return deltaX == 0 || deltaY == 0 || deltaX == deltaY;
	}


	List<BloodSpawn> bloodSpawns = new ArrayList<>();

	TickDelay bloodSpawnDelay = new TickDelay();

	private void spawnBloodSpawns() {
		if (bloodSpawnDelay.remaining() > 1)
			return;
		bloodSpawnDelay.delay(30);
		int amountToSpawn = 1;
		if (invocations.contains(Invocations.BLOOD_THINNERS))
			amountToSpawn = 3;
		for (int i = 0; i < amountToSpawn; i++) {
			BloodSpawn bloodSpawn =
					(BloodSpawn) new BloodSpawn(8367).spawn(bloodSpawnSpawnBounds.randomPositionWithClipping());
			bloodSpawn.getCombat().setAllowRetaliate(false);
			bloodSpawns.add(bloodSpawn);
		}
	}
}
