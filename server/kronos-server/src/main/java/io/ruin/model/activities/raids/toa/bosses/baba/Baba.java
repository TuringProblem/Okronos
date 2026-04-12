package io.ruin.model.activities.raids.toa.bosses.baba;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.activities.raids.toa.Invocations;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.Hit;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.skills.prayer.Prayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class Baba extends NPCCombat {
	Position boulderPositionOne;
	Position boulderPositionTwo;
	Position boulderPositionThree;
	Position boulderPositionFour;
	Position boulderPositionFive;
	Position babaBoulderPosition;
	boolean inBoulderMode = false;
	List<Sarcophagus> sarcophagusList = new ArrayList<>();

	public int bouldersKilled = 0;
	public boolean attackedNonWeakBoulder = false;
	boolean damagedPlayer = false;

	@Override
	public void init() {
		babaBoulderPosition = new Position(npc.getPosition().getRegion().baseX + 39, npc.getPosition().getRegion().baseY + 31, npc.getPosition().getZ());
		boulderPositionOne = new Position(npc.getPosition().getRegion().baseX + 41, npc.getPosition().getRegion().baseY + 37, npc.getPosition().getZ());
		boulderPositionTwo = new Position(npc.getPosition().getRegion().baseX + 41, npc.getPosition().getRegion().baseY + 34, npc.getPosition().getZ());
		boulderPositionThree = new Position(npc.getPosition().getRegion().baseX + 41, npc.getPosition().getRegion().baseY + 31, npc.getPosition().getZ());
		boulderPositionFour = new Position(npc.getPosition().getRegion().baseX + 41, npc.getPosition().getRegion().baseY + 28, npc.getPosition().getZ());
		boulderPositionFive = new Position(npc.getPosition().getRegion().baseX + 41, npc.getPosition().getRegion().baseY + 25, npc.getPosition().getZ());
		bossBounds = new Bounds(npc.getPosition().getRegion().baseX + 22, npc.getPosition().getRegion().baseY + 25, npc.getPosition().getRegion().baseX + 42, npc.getPosition().getRegion().baseY + 38, npc.getPosition().getZ());

		Sarcophagus sarcophagus = new Sarcophagus(7609, 7608, new Position(3305, 5180, 0), new Bounds(3300, 5175, 3310, 5185, 0));
		Sarcophagus sarcophagusTwo = new Sarcophagus(7609, 7608, new Position(3305, 5180, 0), new Bounds(3300, 5175, 3310, 5185, 0));
		sarcophagusList.add(sarcophagus);
		sarcophagusList.add(sarcophagusTwo);
		npc.hitListener = new HitListener()
			.preDefend(this::preDefend)
			.postDamage(this::postDamage);

	}

	private static String format(long millis) {
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes);
		return String.format("%02d:%02d", minutes, seconds);
	}

	int rocksSent = 0;

	private void postDamage(Hit hit) {
		if (getHpPercentage() < 60 && rocksSent == 0)
			spawnBoulders();
		else if (getHpPercentage() < 32 && rocksSent == 1)
			spawnBoulders();

		if (getHpPercentage() < 60 && Random.get(6) == 0 && !baboonsSpawned) {
			spawnBaboons();
			baboonsSpawned = true;
		}

		if (getHpPercentage() < 90 && rockSpecialsUsed == 0)
			rockfallSpecial();
		else if (getHpPercentage() < 24 && rockSpecialsUsed == 1)
			rockfallSpecial();
	}

	private int getHpPercentage() {
		return (npc.getHp() * 100) / npc.getMaxHp();
	}

	private void preDefend(Hit hit) {
		if (hit.attacker != null) {
			Player hitter = hit.attacker.player;
			if (hitter != null) {
				if (hitter.getCurrentToARaid() != null) {
					hit.boostDefence(hitter.getCurrentToARaid().getBabaPathLevel() * 1.1);
				}
			}
		}
	}

	@Override
	public void follow() {
		follow(1);
	}

	boolean baboonsSpawned = false;

	@Override
	public boolean attack() {
		if (inBoulderMode) {
			return false;
		}
		if (withinDistance(4)) {
			if (shockWaveActive) {
				return false;
			}
			if (Random.get(8) == 0) {
				shockwaveSpecial(target);
				return true;
			}
			autoAttack();
		} else npc.getRouteFinder().routeAbsolute(target.getPosition().getX(), target.getPosition().getY());
		return true;
	}

	private void autoAttack() {
		npc.face(target);
		npc.animate(9743);
		int maxHit = 47;
		if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE)) {
			maxHit = 14;

			if (target.player != null && target.player.getCurrentToARaid() != null && target.player.getCurrentToARaid().getInvocations().contains(Invocations.QUIET_PRAYERS))
				maxHit = 18;
		} else {
			damagedPlayer = true;
		}
		Hit hit = new Hit(npc, AttackStyle.CRUSH, AttackType.AGGRESSIVE).randDamage(maxHit).ignorePrayer();
		target.hit(hit);
	}

	boolean invocationsSet = false;
	public ActivityTimer timer;
	boolean timerSet = false;

	@Override
	public void process() {
		if (!npc.localPlayers().isEmpty()) {
			if (!timerSet) {
				timer = new ActivityTimer();
				timerSet = true;
			}
			if (!invocationsSet) {
				Player player = npc.localPlayers().getFirst();
				invocationsSet = true;
				invocations = player.getCurrentToARaid().getInvocations();
				;
			}
		}
		if (target == null && !npc.getPosition().getRegion().players.isEmpty() && !inBoulderMode) {
			target = Random.get(npc.getPosition().getRegion().players);
		}
		if (!baboonSpawns.isEmpty()) {
			for (int i = baboonSpawns.size() - 1; i >= 0; i--) {
				BaboonSpawn baboonSpawn = baboonSpawns.get(i);
				if (baboonSpawn.getHp() < 1) {
					baboonSpawn.remove();
					baboonSpawns.remove(baboonSpawn);
				} else {
					baboonSpawn.process(npc, getActiveSarcophagus());

				}
			}
		}
		if (!sarcophagusList.isEmpty()) {
			for (Sarcophagus sarcophagus : sarcophagusList) {
				int damage = 12;
				if (invocations.contains(Invocations.GOTTA_HAVE_FAITH))
					damage = 20;
				sarcophagus.process(npc.localPlayers(), npc, damage);
			}
		}
	}

	private List<Sarcophagus> getActiveSarcophagus() {
		List<Sarcophagus> activeSarcophagus = new ArrayList<>();
		for (Sarcophagus sarcophagus : sarcophagusList) {
			if (sarcophagus.npc.getHp() > 0) {
				activeSarcophagus.add(sarcophagus);
			}
		}
		return activeSarcophagus;
	}

	private void spawnBaboons() {
		int amount = 2;
		if (npc.localPlayers().size() > 2) {
			amount = 3;
		}
		for (int i = 0; i < amount; i++) {
			BaboonSpawn baboonSpawn = (BaboonSpawn) new BaboonSpawn(7608).spawn(bossBounds.randomPosition());
			baboonSpawns.add(baboonSpawn);
		}
	}

	List<BaboonSpawn> baboonSpawns = new ArrayList<>();
	boolean shockWaveActive = false;

	private void shockwaveSpecial(Entity target) {
		if (shockWaveActive) {
			return;
		}
		shockWaveActive = true;
		World.startEvent(event -> {
			event.delay(2);

			// Calculate direction from NPC to target
			int dx = target.getPosition().getX() - npc.getCentrePosition().getX();
			int dy = target.getPosition().getY() - npc.getCentrePosition().getY();

			// Move two tiles towards the target
			Position startPos = new Position(npc.getCentrePosition().getX() + (dx > 0 ? 2 : (dx < 0 ? -2 : 0)),
				npc.getCentrePosition().getY() + (dy > 0 ? 2 : (dy < 0 ? -2 : 0)),
				npc.getCentrePosition().getZ());

			List<Position> shockwaveTiles = calculateShockwaveTiles(target);

			World.sendGraphics(2111, 0, 0, startPos);
			for (int i = -1; i < 1; i++) {
				World.sendGraphics(2111, 0, 10, new Position(startPos.getX() + i, startPos.getY(), startPos.getZ()));
				World.sendGraphics(2111, 0, 10, new Position(startPos.getX(), startPos.getY() + i, startPos.getZ()));
			}
			event.delay(2);

			World.sendGraphics(2111, 0, 0, startPos);
			for (int i = -1; i < 1; i++) {
				World.sendGraphics(2111, 0, 0, new Position(startPos.getX() + i, startPos.getY(), startPos.getZ()));
				World.sendGraphics(2111, 0, 0, new Position(startPos.getX(), startPos.getY() + i, startPos.getZ()));
			}
			event.delay(3);

			npc.animate(9749);
			for (Position tile : shockwaveTiles) {
				World.sendGraphics(2257, 0, 0, tile);
				npc.localPlayers().forEach(p -> {
					if (p.getPosition().equals(tile)) {
						damagedPlayer = true;
						p.hit(new Hit().fixedDamage(25));
					}
				});
			}
			event.delay(1);
			shockWaveActive = false;
		});
	}

	boolean rocksActive = false;
	int rockSpecialsUsed = 0;
	Bounds bossBounds;
	List<NPC> activeBoulders = new ArrayList<>();
	Projectile boulderProjectile = new Projectile(2179, 79, 0, 20, 15, 66, 15, 10);

	private void rockfallSpecial() {
		if (rocksActive) {
			return;
		}
		int graphicsId;
		int delay;
		Player player = npc.getPosition().getRegion().players.getFirst();
		if (player != null && player.getCurrentToARaid() != null) {
			if (player.getCurrentToARaid().getBabaPathLevel() >= 4) {
				graphicsId = 2252;
				delay = 1;
			} else if (player.getCurrentToARaid().getBabaPathLevel() >= 2) {
				graphicsId = 2251;
				delay = 2;
			} else {
				delay = 3;
				graphicsId = 2250;
			}
		} else {
			delay = 3;
			graphicsId = 2250;
		}
		rockSpecialsUsed++;
		rocksActive = true;
		World.startEvent(e -> {
			e.setCancelCondition(() -> inBoulderMode);
			Position BoulderPositionOne = bossBounds.randomPosition();
			Position BoulderPositionTwo = getPositionAway(BoulderPositionOne, 5, bossBounds);
			World.sendGraphics(graphicsId, 0, 0, BoulderPositionOne);
			World.sendGraphics(graphicsId, 0, 0, BoulderPositionTwo);
			e.delay(delay);
			NPC boulderOne = new NPC(11784).spawn(BoulderPositionOne);
			NPC boulderTwo = new NPC(11784).spawn(BoulderPositionTwo);
			npc.getPosition().getRegion().players.forEach(p -> {
				if (p.getPosition().distance(boulderPositionOne) < 2 || p.getPosition().distance(boulderPositionTwo) < 2)
					p.hit(new Hit().randDamage(25, 40));
			});
			activeBoulders.add(boulderOne);
			activeBoulders.add(boulderTwo);
			e.delay(18);
			npc.animate(9744);

			for (Player p : npc.getPosition().getRegion().players) {
				sendBouldersToPlayers(p);
			}
			e.delay(18);
			for (int i = activeBoulders.size() - 1; i >= 0; i--) {
				activeBoulders.get(i).remove();
				activeBoulders.remove(i);
			}
			rocksActive = false;
		});
	}

	private void sendBouldersToPlayers(Player p) {
		World.startEvent(e -> {
			int delay = boulderProjectile.send(npc, p);
			e.delay(World.getTicks(delay));
			List<String> safePlayers = getSafePlayers(npc.getPosition().getRegion().players.size());
			if (safePlayers.contains(p.getName())) {
				p.hit(new Hit().randDamage(5));
			} else {
				damagedPlayer = true;
				p.hit(new Hit().randDamage(25, 40));
			}
		});
	}

	private List<String> getSafePlayers(int amount) {
		List<String> safePlayers = new ArrayList<>();
		for (NPC boulder : activeBoulders) {
			int safePlayersCount = 0;
			int maxSafePlayers = amount / 2;
			for (Player p : npc.getPosition().getRegion().players) {
				if (p.getPosition().inBounds(bossBounds)) {
					if (p.getPosition().distance(boulder.getPosition()) < 2) {
						safePlayers.add(p.getName());
						safePlayersCount++;
						if (safePlayersCount >= maxSafePlayers) {
							break;
						}
					}
				}
			}
		}
		return safePlayers;
	}

	private Position getPositionAway(Position position, int minDistance, Bounds bossBounds) {
		int angle = Random.get(0, 360); // Random angle in degrees
		int distance = Random.get(minDistance, bossBounds.getDiagonalSize());

		int xOffset = (int) (Math.cos(Math.toRadians(angle)) * distance);
		int yOffset = (int) (Math.sin(Math.toRadians(angle)) * distance);

		int newX = position.getX() + xOffset;
		int newY = position.getY() + yOffset;

		newX = Math.max(bossBounds.swX, Math.min(newX, bossBounds.neX));
		newY = Math.max(bossBounds.swY, Math.min(newY, bossBounds.neY));

		return new Position(newX, newY, position.getZ());
	}


	private List<Position> calculateShockwaveTiles(Entity target) {
		int centerX = target.getPosition().getX();
		int centerY = target.getPosition().getY();
		int radius = 2;
		if (invocations.contains(Invocations.SHAKING_THINGS_UP))
			radius = 3;


		//5x5 grid centreX and CentreY being middle tile and also one tile extra tile on top middle, bottom middle, left middle and right middle
		List<Position> tiles = new ArrayList<>();

		for (int x = centerX - radius; x <= centerX + radius; x++) {
			for (int y = centerY - radius; y <= centerY + radius; y++) {
				tiles.add(new Position(x, y, npc.getPosition().getZ()));
			}
		}
		return tiles;
	}


	@Override
	public void startDeath(Hit killHit) {
		if (npc.getPosition().getRegion().players.isEmpty())
			return;
		Player player = npc.getPosition().getRegion().players.getFirst();
		long tumekenTime = timer.stop(player, -1);
		if (bouldersKilled <= 4) {
			npc.getPosition().getRegion().players.forEach(p -> {
				if (p.getCurrentToARaid() != null && !p.getCurrentToARaid().deadMembers.contains(p.getName())) {
					Objects.requireNonNull(p.combatAchievementsList
						.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.IM_IN_A_RUSH.ordinal()))
						.getCombatAchievement()).check(p);
				}
			});
		}
		if (!attackedNonWeakBoulder && invocations.contains(Invocations.BOULDERDASH)) {
			npc.getPosition().getRegion().players.forEach(p -> {
				if (p.getCurrentToARaid() != null && !p.getCurrentToARaid().deadMembers.contains(p.getName())) {
					Objects.requireNonNull(p.combatAchievementsList
						.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.NO_SKIPPING_ALLOWED.ordinal()))
						.getCombatAchievement()).check(p);
				}
			});
		}
		player.getCurrentToARaid().teleportDeadPlayers();
		npc.getPosition().getRegion().players.forEach(p -> {
			if (p != null) {
				if (p.getCurrentToARaid() != null && p.getCurrentToARaid().getBabaPathLevel() >= 4
					&& !p.getCurrentToARaid().deadMembers.contains(p.getName())) {
					if (invocations.contains(Invocations.BOULDERDASH) && invocations.contains(Invocations.MIND_THE_GAP) &&
						invocations.contains(Invocations.SHAKING_THINGS_UP) && invocations.contains(Invocations.JUNGLE_JAPES)) {
						Objects.requireNonNull(p.combatAchievementsList
							.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.BABANANZA.ordinal()))
							.getCombatAchievement()).check(p);
					}

				}
				if (!damagedPlayer && p.getCurrentToARaid() != null && !p.getCurrentToARaid().deadMembers.contains(p.getName())
					&& p.getCurrentToARaid().currentParty.getMembers().size() > 1) {
					if (invocations.contains(Invocations.BOULDERDASH) && invocations.contains(Invocations.MIND_THE_GAP) &&
						invocations.contains(Invocations.SHAKING_THINGS_UP) && invocations.contains(Invocations.JUNGLE_JAPES)) {
						Objects.requireNonNull(p.combatAchievementsList
							.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_BABA.ordinal()))
							.getCombatAchievement()).check(p);
						if (p.getCurrentToARaid() != null) {
							p.getCurrentToARaid().perfectBaba = true;
						}
					}

				}
				p.getCombat().restore();
				p.getStats().restore(false);
				p.cureVenom(0);
				p.getCombat().restoreSpecial(100);
				p.getCombat().reset();
				if (p.babaBestTime == -1) {
					p.sendMessage("Duration: <col=ef1020>" + format(tumekenTime));
					p.babaBestTime = tumekenTime;
					return;
				} else if (p.babaBestTime == 0 || tumekenTime < p.babaBestTime) {
					p.sendMessage("Duration: <col=ef1020>" + format(tumekenTime) + "</col> (new personal best)");
					p.babaBestTime = tumekenTime;
					return;
				} else {
					p.sendMessage(
						"Duration: <col=ef1020>" + format(tumekenTime) + "</col>. Personal best: " + format(p.babaBestTime));
					return;
				}
			}
		});
		NPC osmumten = new NPC(11689).spawn(npc.getPosition().getRegion().baseX + 37,
			npc.getPosition().getRegion().baseY + 30, npc.getHeight());
		osmumten.face(Direction.WEST);
		npc.remove();
		sarcophagusList.forEach(sarcophagus -> sarcophagus.npc.remove());
		baboonSpawns.forEach(baboonSpawn -> baboonSpawn.npc.remove());
	}

	@Override
	public int getAggressionRange() {
		return 40;
	}

	@Override
	public int getAttackBoundsRange() {
		return 40;
	}

	List<Invocations> invocations = new ArrayList<>();

	private void spawnBoulders() {
		if (inBoulderMode) {
			return;
		}
		rocksSent++;
		inBoulderMode = true;
		npc.getCombat().getInfo().aggressive_level = 1;
		npc.getCombat().reset();
		rocksActive = false;
		for (int i = activeBoulders.size() - 1; i >= 0; i--) {
			activeBoulders.get(i).remove();
			activeBoulders.remove(i);
		}
		npc.getMovement().teleport(babaBoulderPosition);
		npc.getCombat().reset();
		npc.getCombat().setAllowRetaliate(false);
		npc.face(Direction.EAST);
		npc.getPosition().getRegion().players.forEach(p -> {
			if (p.getCurrentToARaid() != null && !p.getCurrentToARaid().deadMembers.contains(p.getName()) && p.getPosition().inBounds(bossBounds)) {
				p.getCombat().reset();
				Position targetPosition = new Position(p.getPosition().getRegion().baseX + 22, p.getPosition().getY(), p.getPosition().getZ());
				boolean playingDying;
				if (invocations.contains(Invocations.MIND_THE_GAP)) {
					int y;
					int baseY = p.getPosition().getRegion().baseY;
					y = p.getPosition().getY() - baseY;
					if (y > 29 && y < 35) {
						targetPosition = new Position(p.getPosition().getRegion().baseX + 21, p.getPosition().getY(), p.getPosition().getZ());
						playingDying = true;
					} else {
						playingDying = false;
					}

				} else {
					playingDying = false;
				}
				final int[] distance = {p.getPosition().distance(targetPosition)};
				Position finalTargetPosition = targetPosition;
				World.startEvent(e -> {
					while (distance[0] > 0) {
						p.lock();
						if (p.getHp() < 1) {
							break;
						}
						Position teleportPosition = new Position(p.getPosition().getX() - 3, p.getPosition().getY(), p.getPosition().getZ());
						if (distance[0] <= 6)
							teleportPosition = finalTargetPosition;
						p.getMovement().teleport(teleportPosition);

						p.animate(9799);
						p.graphics(245, 5, 10);
						distance[0] = p.getPosition().distance(finalTargetPosition);
						if (distance[0] == 0) {
							if (!playingDying)
								p.unlock();
							else {
								p.hit(new Hit().fixedDamage(p.getHp()));
							}
							break;
						}
						e.delay(1);

					}
				});
			}
		});
		World.startEvent(e -> {
			e.setCancelCondition(() -> npc.getHp() < 1);
			for (int i = 0; i < 10; i++) {
				if (npc.getHp() < 1) break;
				npc.face(Direction.EAST);
				int delay = 11;
				if (invocations.contains(Invocations.BOULDERDASH))
					delay = 7;
				e.delay(i == 0 ? 4 : delay);
				npc.face(Direction.EAST);
				List<Position> boulderPositions = new ArrayList<>();
				boulderPositions.add(boulderPositionOne);
				boulderPositions.add(boulderPositionTwo);
				boulderPositions.add(boulderPositionThree);
				boulderPositions.add(boulderPositionFour);
				boulderPositions.add(boulderPositionFive);
				for (int j = 0; j < 4; j++) {
					Position spawn = Random.get(boulderPositions);
					boulderPositions.remove(spawn);
					Boulder boulder = new Boulder(11782, spawn, npc);
					if (npc.getPosition().getRegion().players.isEmpty())
						break;
					Player player = npc.getPosition().getRegion().players.getFirst();
					if (player != null && player.getCurrentToARaid() != null) {
						if (player.getCurrentToARaid().getBabaPathLevel() >= 4) {
							player.getCurrentToARaid().scaleNPC(boulder, 4);
						}

					}
				}
				if (npc.getPosition().getRegion().players.isEmpty())
					return;
				Boulder boulder = new Boulder(11783, boulderPositions.get(0), npc);
				Player player = npc.getPosition().getRegion().players.getFirst();
				if (player != null && player.getCurrentToARaid() != null) {
					if (player.getCurrentToARaid().getBabaPathLevel() >= 4) {
						player.getCurrentToARaid().scaleNPC(boulder, 4);
					}
				}
				if (i == 9) {
					e.delay(3);
					npc.getCombat().setAllowRetaliate(true);
					inBoulderMode = false;
					npc.getCombat().getInfo().aggressive_level = 1000;
					target = Random.get(npc.getPosition().getRegion().players);
					npc.getCombat().setTarget(target);
					System.out.println("Boulder mode ended");
					npc.step(-2, 0, StepType.WALK);
				}
			}
		});
	}
}
