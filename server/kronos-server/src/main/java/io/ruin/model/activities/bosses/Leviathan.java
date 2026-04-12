package io.ruin.model.activities.bosses;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.TickDelay;

import java.util.*;

public class Leviathan extends NPCCombat {

	Projectile meleeOrb = new Projectile(2488, 137, 25, 30, 56, 10, 15, 220);
	Projectile rangeOrb = new Projectile(2487, 137, 25, 30, 56, 10, 15, 220);
	Projectile mageOrb = new Projectile(2489, 137, 25, 30, 56, 10, 15, 220);
	private final int ROAR_ANIM = 10282;
	public int rallyTickDelay = 3;
	public int totalRallyStyles = 2;
	public int rallyCount = 6;
	public int order = 1;
	TickDelay meleeAttackDelay = new TickDelay();

	private Bounds[] BOSS_AREA;
	Bounds eastBounds;
	Bounds westBounds;
	Bounds southBounds;
	Bounds northBounds;
	Bounds northEastBounds;
	Bounds northWestBounds;
	Bounds southEastBounds;
	Bounds southWestBounds;

	public boolean inSpecial = false;
	public boolean inRally = false;
	public boolean unconventionalFailed = false;
	public int specialsDone = 0;

	public boolean damagedPlayer = false;

	@Override
	public void init() {
		npc.hitListener = new HitListener().preDefend(this::preDefend).postDefend(this::postDefend)
				.postDamage(this::postDamage);
		npc.deathEndListener = (entity, killer, killHit) -> {
			for (GameObject rock : rocks) {
				rock.remove();
			}
		};
		BOSS_AREA = new Bounds[] {
				new Bounds(new Position(npc.getPosition().getRegion().baseX + 24, npc.getPosition().getRegion().baseY + 26, 0),
						new Position(npc.getPosition().getRegion().baseX + 28, npc.getPosition().getRegion().baseY + 47, 0), 0),
				new Bounds(new Position(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 40, 0),
						new Position(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 47, 0), 0),
				new Bounds(new Position(npc.getPosition().getRegion().baseX + 38, npc.getPosition().getRegion().baseY + 27, 0),
						new Position(npc.getPosition().getRegion().baseX + 43, npc.getPosition().getRegion().baseY + 47, 0), 0),
				new Bounds(new Position(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 25, 0),
						new Position(npc.getPosition().getRegion().baseX + 36, npc.getPosition().getRegion().baseY + 33, 0), 0)

		};
		northBounds = new Bounds(
				new Position(npc.getPosition().getRegion().baseX + 30, npc.getPosition().getRegion().baseY + 40, 0),
				new Position(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 47, 0), 0);
		northEastBounds = new Bounds(
				new Position(npc.getPosition().getRegion().baseX + 36, npc.getPosition().getRegion().baseY + 40, 0),
				new Position(npc.getPosition().getRegion().baseX + 45, npc.getPosition().getRegion().baseY + 47, 0), 0);
		eastBounds = new Bounds(
				new Position(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 33, 0),
				new Position(npc.getPosition().getRegion().baseX + 44, npc.getPosition().getRegion().baseY + 40, 0), 0);
		southEastBounds = new Bounds(
				new Position(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 27, 0),
				new Position(npc.getPosition().getRegion().baseX + 46, npc.getPosition().getRegion().baseY + 32, 0), 0);
		southBounds = new Bounds(
				new Position(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 22, 0),
				new Position(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 34, 0), 0);
		southWestBounds = new Bounds(
				new Position(npc.getPosition().getRegion().baseX + 22, npc.getPosition().getRegion().baseY + 26, 0),
				new Position(npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 33, 0), 0);
		westBounds = new Bounds(
				new Position(npc.getPosition().getRegion().baseX + 21, npc.getPosition().getRegion().baseY + 33, 0),
				new Position(npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 39, 0), 0);
		northWestBounds = new Bounds(
				new Position(npc.getPosition().getRegion().baseX + 20, npc.getPosition().getRegion().baseY + 40, 0),
				new Position(npc.getPosition().getRegion().baseX + 30, npc.getPosition().getRegion().baseY + 47, 0), 0);

	}

	private void postDamage(Hit hit) {
		if (hit.attackStyle != null && hit.attackStyle.isRanged()) {
			if (hit.attacker != null && hit.attacker.isPlayer()) {

			}
		} else
			unconventionalFailed = true;
	}

	private void postDefend(Hit hit) {

	}

	private void preDefend(Hit hit) {
		if (inSpecial) {
			hit.boostDefence(1.33);
		}
	}

	@Override
	public void follow() {

	}

	@Override
	public boolean attack() {
		if (inSpecial)
			return true;
		if (withinDistance(2) && meleeAttackDelay.finished()) {
			bite();
		}
		if (inRally)
			return true;

		sendOrbRally();
		return true;
	}

	private void bite() {
		meleeAttackDelay.delay(4);
		npc.animate(10284);
		if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE)) {
			target.hit(new Hit().fixedDamage(0));
		} else {
			target.hit(new Hit().randDamage(25, 40));
			damagedPlayer = true;
		}
	}

	@Override
	public void process() {
		if (npc.getHp() < 1) {
			rocks.forEach(GameObject::remove);
		}
		if (target != null && target.isPlayer()) {
			if (target.player.getHp() > 25)
				unconventionalFailed = true;
		}
	}

	Projectile rockThrowProjectile = new Projectile(2472, 187, 0, 30, 56, 13, 33, 220);
	List<GameObject> rocks = new ArrayList<>();

	private void rockSpecial() {
		if (inSpecial)
			return;
		inSpecial = true;
		World.startEvent(e -> {
			e.setCancelCondition(() -> npc.getHp() < 1 || targetIsNotInBossRegion());
			for (int i = 0; i < 6; i++) {
				if (target == null)
					break;
				Position targetPos = target.getPosition().copy();
				npc.animate(10288);
				int delay = rockThrowProjectile.send(npc, targetPos);
				e.delay(World.getTicks(delay) + 1);
				if (target.getPosition().regionId() != npc.getPosition().regionId())
					return;
				if (target.getPosition().distance(targetPos) < 1) {
					target.hit(new Hit().randDamage(25, 40));
					damagedPlayer = true;
				}
				if (target.getPosition().regionId() != npc.getPosition().regionId())
					return;
				GameObject rock = new GameObject(47590, targetPos.getX(), targetPos.getY(), targetPos.getZ(), 10, 0).spawn();
				rocks.add(rock);
				e.delay(2);
			}
			e.delay(15);
			List<Position> impactPositions = new ArrayList<>();
			List<Position> safePositions = getSafeTiles();
			for (Bounds bounds : BOSS_AREA) {
				for (Position position : bounds.getAllPositions()) {
					if (safePositions.contains(position))
						continue;
					impactPositions.add(position);
				}
			}
			for (Position pos : impactPositions) {
				World.sendGraphics(1243, 0, 0, pos);
				if (target != null && target.getPosition().distance(pos) < 1) {
					target.hit(new Hit().randDamage(25, 40));
					damagedPlayer = true;
				}
			}
			inSpecial = false;
		});
	}

	List<Position> getSafeTiles() {
		List<Position> safeTiles = new ArrayList<>();
		for (GameObject rock : rocks) {
			Position rockPos = rock.getPosition();
			Direction directionToBoss = Direction.getDirection(rockPos, npc.getPosition());
			Position safeTile = switch (directionToBoss) {
				case NORTH -> new Position(rockPos.getX(), rockPos.getY() + 1, rockPos.getZ());
				case SOUTH -> new Position(rockPos.getX(), rockPos.getY() - 1, rockPos.getZ());
				case EAST -> new Position(rockPos.getX() + 1, rockPos.getY(), rockPos.getZ());
				case WEST -> new Position(rockPos.getX() - 1, rockPos.getY(), rockPos.getZ());
				case NORTH_EAST -> new Position(rockPos.getX() + 1, rockPos.getY() + 1, rockPos.getZ());
				case NORTH_WEST -> new Position(rockPos.getX() - 1, rockPos.getY() + 1, rockPos.getZ());
				case SOUTH_EAST -> new Position(rockPos.getX() + 1, rockPos.getY() - 1, rockPos.getZ());
				case SOUTH_WEST -> new Position(rockPos.getX() - 1, rockPos.getY() - 1, rockPos.getZ());
			};

			safeTiles.add(safeTile);
		}
		return safeTiles;
	}

	private void roarRockFall() {
		World.startEvent(e -> {
			if (target == null) {
				return;
			}
			e.setCancelCondition(() -> npc.getHp() < 1 || targetIsNotInBossRegion());
			target.hit(new Hit(npc).randDamage(5, 11));
			npc.animate(ROAR_ANIM);
			if (order < 5)
				order++;
			switch (order) {
				case 2:
					rallyTickDelay = 2;
					rallyCount = 8;
					break;
				case 3:
					totalRallyStyles = 3;
					break;
				case 4:
					rallyTickDelay = 1;
					totalRallyStyles = 2;
					rallyCount = 12;
					break;
				case 5:
					rallyTickDelay = 1;
					totalRallyStyles = 3;
					rallyCount = 12;
					break;
			}
			List<Position> rockSpawnPositions = new ArrayList<>();
			for (int i = 0; i < 13; i++) {
				Position randomPos = BOSS_AREA[Random.get(BOSS_AREA.length - 1)].randomPosition();
				World.sendGraphics(2480, 0, 0, randomPos);
				rockSpawnPositions.add(randomPos);
			}
			e.delay(2);
			for (Position position : rockSpawnPositions) {
				GameObject rock = new GameObject(47590, position.getX(), position.getY(), position.getZ(), 10, 0).spawn();
				rocks.add(rock);
				if (target.getPosition().distance(position) < 1) {
					target.hit(new Hit().randDamage(25, 40));
					damagedPlayer = true;
				}
			}
			Position targetPosition = target.getPosition().copy();
			World.sendGraphics(2480, 0, 0, targetPosition);
			e.delay(2);
			if (target.getPosition().distance(targetPosition) < 1) {
				target.hit(new Hit().randDamage(25, 40));
				damagedPlayer = true;
			}
			if (target.getPosition().regionId() != npc.getPosition().regionId())
				return;
			GameObject rock = new GameObject(47590, targetPosition.getX(), targetPosition.getY(), targetPosition.getZ(), 10,
					0).spawn();
			rocks.add(rock);
			e.delay(2);
			inRally = false;
			if (npc.getHp() < 2500 && specialsDone == 0) {
				int random = Random.get(1, 2);
				switch (random) {
					case 1:
						rockSpecial();
						break;
					case 2:
						lightningSpecial();
						break;
				}
				specialsDone++;
			}
			if (npc.getHp() < 1300 && specialsDone == 1) {
				int random = Random.get(1, 2);
				switch (random) {
					case 1:
						rockSpecial();
						break;
					case 2:
						lightningSpecial();
						break;
				}
				specialsDone++;
			}
		});
	}

	int rallysSinceSpecial = 0;

	private void sendOrbRally() {
		if (inSpecial || inRally)
			return;
		inRally = true;
		World.startEvent(e -> {
			e.setCancelCondition(() -> npc.getHp() < 1 || targetIsNotInBossRegion());
			for (int i = 0; i < rallyCount; i++) {
				e.delay(3);
				sendRandomOrb();
				int finalIndex = rallyCount - 1;
				if (i == finalIndex) {
					e.delay(3);
					roarRockFall();
				}
			}
		});
	}

	private void sendMeleeOrb() {
		if (target == null)
			return;
		npc.graphics(2485, 0, 0);
		World.startEvent(e -> {
			e.setCancelCondition(() -> npc.getHp() < 1 || targetIsNotInBossRegion());
			int delay = meleeOrb.send(npc, target);
			e.delay(World.getTicks(delay) + 1);
			if (target != null) {
				if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE)) {
					target.hit(new Hit().fixedDamage(0));
				} else {
					target.hit(new Hit().randDamage(25, 40));
					damagedPlayer = true;
				}
			}
		});
	}

	Projectile lightningBallProjectile = new Projectile(2506, 137, 0, 30, 56, 10, 15, 220);

	private void sendLightningBall(Position randomPos) {
		World.startEvent(e -> {
			e.setCancelCondition(() -> npc.getHp() < 1 || targetIsNotInBossRegion());
			Player target;
			int delay = lightningBallProjectile.send(npc, randomPos);
			e.delay(World.getTicks(delay) + 1);
			World.sendGraphics(2504, 0, 0, randomPos);
			if (npc.getPosition().getRegion().players.isEmpty())
				return;
			target = npc.getPosition().getRegion().players.get(0);
			if (target.getPosition().distance(randomPos) < 1) {
				damagedPlayer = true;
				target.hit(new Hit().randDamage(15, 20));
			}
		});
	}

	private Position getTileInFront() {
		int npcX = npc.getPosition().getX();
		int npcY = npc.getPosition().getY();
		int direction = npc.getDirection(); // Assuming NPC's direction is stored as degrees (0-360)

		// Calculate the offset based on the direction
		int offsetX = (int) Math.round(Math.sin(Math.toRadians(direction)));
		int offsetY = (int) Math.round(Math.cos(Math.toRadians(direction)));

		// Add the offset to the NPC's current tile coordinates
		int newX = npcX + offsetX;
		int newY = npcY + offsetY;

		// Return the position of the tile in front of the NPC
		return new Position(newX, newY, npc.getPosition().getZ());
	}

	private List<Position> getTriangleFormationInFront(int length, int width) {
		List<Position> positions = new ArrayList<>();
		Position frontTile = getTileInFront();
		// System.out.println("Front tile: " + frontTile);

		// Calculate the offsets based on the NPC's direction
		int offsetX = (int) Math.round(Math.sin(Math.toRadians(npc.getDirection())));
		int offsetY = (int) Math.round(Math.cos(Math.toRadians(npc.getDirection())));

		// Calculate positions for the triangle based on the front tile and offsets
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < length; j++) {
				int x = frontTile.getX() + offsetX * (length - j) + offsetY * (width / 2 - i);
				int y = frontTile.getY() + offsetY * (length - j) - offsetX * (width / 2 - i);
				positions.add(new Position(x, y, frontTile.getZ()));
			}
		}
		return positions;
	}

	private void lightningSpecial() {
		if (inSpecial)
			return;
		// System.out.println("lightning started but in rally? " + inRally);
		inSpecial = true;
		World.startEvent(e -> {
			npc.animate(10285);
			e.setCancelCondition(() -> npc.getHp() < 1 || targetIsNotInBossRegion());
			e.delay(1);
			npc.animate(10286);
			for (int i = 0; i < 17; i++) {
				int lightningBallsSent = 0;
				while (lightningBallsSent < 8) {
					Position randomPos = BOSS_AREA[Random.get(BOSS_AREA.length - 1)].randomPosition();
					if (randomPos.distance(npc.getPosition()) < 2)
						continue;
					sendLightningBall(randomPos);
					lightningBallsSent++;
				}
				if (npc.getPosition().getRegion().players.isEmpty())
					return;
				target = npc.getPosition().getRegion().players.getFirst();
				if (target == null) {
					continue;
				}
				npc.face(target);
				// Direction faceDir = Direction.getDirection(getLightningStartPosition(target),
				// target.getPosition());
				List<Position> lightningPositions = new ArrayList<>();
				// System.out.println("face dir: " + faceDir);

				if (target.getPosition().inBounds(eastBounds)) {
					lightningPositions = getEastLightningPositions(target);
				} else if (target.getPosition().inBounds(westBounds)) {
					lightningPositions = getWestLightningPositions(target);
				} else if (target.getPosition().inBounds(southBounds)) {
					lightningPositions = getSouthLightningPositions(target);
				} else if (target.getPosition().inBounds(northBounds)) {
					lightningPositions = getNorthLightningPositions(target);
				} else if (target.getPosition().inBounds(northEastBounds)) {
					lightningPositions = getNorthEastLightningPositions(target);
				} else if (target.getPosition().inBounds(northWestBounds)) {
					lightningPositions = getNorthWestLightningPositions(target);
				} else if (target.getPosition().inBounds(southEastBounds)) {
					lightningPositions = getSouthEastLightningPositions(target);
				} else if (target.getPosition().inBounds(southWestBounds)) {
					lightningPositions = getSouthWestLightningPositions(target);
				}

				int rotations = 0;
				e.delay(2);
				for (Position pos : lightningPositions) {
					rotations++;
					World.sendGraphics(2503, 0, rotations, pos);
					if (target == null) {
						continue;
					}
					if (target.getPosition().distance(pos) < 1) {
						damagedPlayer = true;
						target.hit(new Hit().randDamage(17, 25));
					}
				}
				if (i == 16) {
					inSpecial = false;
				}
			}
		});
	}

	public double dot(Position pointA, Position pointB) {
		return pointA.getX() * pointB.getX() + pointA.getY() * pointB.getY();
	}

	private List<Position> getEastLightningPositions(Entity target) {
		List<Position> positions = new ArrayList<>();
		Position startingTile = getLightningStartPosition(target);
		positions.add(startingTile);
		for (int i = 0; i < 2; i++) {
			positions.add(new Position(startingTile.getX() + 1, startingTile.getY() + i, startingTile.getZ()));
		}
		positions.add(new Position(startingTile.getX() + 2, startingTile.getY(), startingTile.getZ()));
		for (int i = 0; i < 2; i++) {
			positions.add(new Position(startingTile.getX() + 3, startingTile.getY() + i, startingTile.getZ()));
		}
		for (int i = 0; i < 3; i++) {
			positions.add(new Position(startingTile.getX() + 4, startingTile.getY() + i, startingTile.getZ()));
		}
		for (int i = 1; i < 4; i++) {
			positions.add(new Position(startingTile.getX() + 5, startingTile.getY() + i, startingTile.getZ()));
			positions.add(new Position(startingTile.getX() + 6, startingTile.getY() + i, startingTile.getZ()));
		}
		for (int i = 1; i < 5; i++) {
			positions.add(new Position(startingTile.getX() + 7, startingTile.getY() + i, startingTile.getZ()));
		}
		for (int i = 1; i < 6; i++) {
			positions.add(new Position(startingTile.getX() + 8, startingTile.getY() + i, startingTile.getZ()));
		}
		return positions;
	}

	private List<Position> getWestLightningPositions(Entity target) {
		List<Position> positions = new ArrayList<>();
		Position startingTile = getLightningStartPosition(target);
		positions.add(startingTile);
		for (int i = 0; i < 2; i++) {
			positions.add(new Position(startingTile.getX() - 1, startingTile.getY() + i, startingTile.getZ()));
		}
		positions.add(new Position(startingTile.getX() - 2, startingTile.getY(), startingTile.getZ()));
		for (int i = 0; i < 2; i++) {
			positions.add(new Position(startingTile.getX() - 3, startingTile.getY() + i, startingTile.getZ()));
		}
		for (int i = 0; i < 3; i++) {
			positions.add(new Position(startingTile.getX() - 4, startingTile.getY() + i, startingTile.getZ()));
		}
		for (int i = 1; i < 4; i++) {
			positions.add(new Position(startingTile.getX() - 5, startingTile.getY() + i, startingTile.getZ()));
			positions.add(new Position(startingTile.getX() - 6, startingTile.getY() + i, startingTile.getZ()));
		}
		for (int i = 1; i < 5; i++) {
			positions.add(new Position(startingTile.getX() - 7, startingTile.getY() + i, startingTile.getZ()));
		}
		for (int i = 1; i < 6; i++) {
			positions.add(new Position(startingTile.getX() - 8, startingTile.getY() + i, startingTile.getZ()));
		}
		return positions;
	}

	private List<Position> getSouthLightningPositions(Entity target) {
		List<Position> positions = new ArrayList<>();
		Position startingTile = getLightningStartPosition(target);
		positions.add(startingTile);
		for (int i = 0; i < 2; i++) {
			positions.add(new Position(startingTile.getX() + i, startingTile.getY() - 1, startingTile.getZ()));
		}
		positions.add(new Position(startingTile.getX(), startingTile.getY() - 2, startingTile.getZ()));
		for (int i = 0; i < 2; i++) {
			positions.add(new Position(startingTile.getX() + i, startingTile.getY() - 3, startingTile.getZ()));
		}
		for (int i = 0; i < 3; i++) {
			positions.add(new Position(startingTile.getX() + i, startingTile.getY() - 4, startingTile.getZ()));
		}
		for (int i = 1; i < 4; i++) {
			positions.add(new Position(startingTile.getX() + i, startingTile.getY() - 5, startingTile.getZ()));
			positions.add(new Position(startingTile.getX() + i, startingTile.getY() - 6, startingTile.getZ()));
		}
		for (int i = 1; i < 5; i++) {
			positions.add(new Position(startingTile.getX() + i, startingTile.getY() - 7, startingTile.getZ()));
		}
		for (int i = 1; i < 6; i++) {
			positions.add(new Position(startingTile.getX() + i, startingTile.getY() - 8, startingTile.getZ()));
		}
		return positions;
	}

	private List<Position> getNorthLightningPositions(Entity target) {
		List<Position> positions = new ArrayList<>();
		Position startingTile = getLightningStartPosition(target);
		positions.add(startingTile);
		for (int i = 0; i < 2; i++) {
			positions.add(new Position(startingTile.getX() - i, startingTile.getY() + 1, startingTile.getZ()));
		}
		positions.add(new Position(startingTile.getX(), startingTile.getY() + 2, startingTile.getZ()));
		for (int i = 0; i < 2; i++) {
			positions.add(new Position(startingTile.getX() - i, startingTile.getY() + 3, startingTile.getZ()));
		}
		for (int i = 0; i < 3; i++) {
			positions.add(new Position(startingTile.getX() - i, startingTile.getY() + 4, startingTile.getZ()));
		}
		for (int i = 1; i < 4; i++) {
			positions.add(new Position(startingTile.getX() - i, startingTile.getY() + 5, startingTile.getZ()));
			positions.add(new Position(startingTile.getX() - i, startingTile.getY() + 6, startingTile.getZ()));
		}
		for (int i = 1; i < 5; i++) {
			positions.add(new Position(startingTile.getX() - i, startingTile.getY() + 7, startingTile.getZ()));
		}
		for (int i = 1; i < 6; i++) {
			positions.add(new Position(startingTile.getX() - i, startingTile.getY() + 8, startingTile.getZ()));
		}
		return positions;
	}

	private List<Position> getSouthWestLightningPositions(Entity target) {
		List<Position> positions = new ArrayList<>();
		Position startingTile = getLightningStartPosition(target);
		for (int i = 0; i < 2; i++) {
			positions.add(new Position(startingTile.getX(), startingTile.getY() - i, startingTile.getZ()));
		}
		for (int i = 0; i < 3; i++) {
			positions.add(new Position(startingTile.getX() - 1, startingTile.getY() - i, startingTile.getZ()));
		}
		for (int i = 0; i < 3; i++) {
			positions.add(new Position(startingTile.getX() - 2, startingTile.getY() - 1 - i, startingTile.getZ()));
		}
		for (int i = 0; i < 4; i++) {
			positions.add(new Position(startingTile.getX() - 3, startingTile.getY() - 2 - i, startingTile.getZ()));
		}
		for (int i = 0; i < 4; i++) {
			positions.add(new Position(startingTile.getX() - 4, startingTile.getY() - 3 - i, startingTile.getZ()));
		}
		for (int i = 0; i < 4; i++) {
			positions.add(new Position(startingTile.getX() - 5, startingTile.getY() - 3 - i, startingTile.getZ()));
		}
		for (int i = 0; i < 3; i++) {
			positions.add(new Position(startingTile.getX() - 5, startingTile.getY() - 4 - i, startingTile.getZ()));
		}
		return positions;
	}

	private List<Position> getSouthEastLightningPositions(Entity target) {
		List<Position> positions = new ArrayList<>();
		Position startingTile = getLightningStartPosition(target);
		for (int i = 0; i < 2; i++) {
			positions.add(new Position(startingTile.getX(), startingTile.getY() - i, startingTile.getZ()));
		}
		for (int i = 0; i < 3; i++) {
			positions.add(new Position(startingTile.getX() + 1, startingTile.getY() - i, startingTile.getZ()));
		}
		for (int i = 0; i < 3; i++) {
			positions.add(new Position(startingTile.getX() + 2, startingTile.getY() - 1 - i, startingTile.getZ()));
		}
		for (int i = 0; i < 4; i++) {
			positions.add(new Position(startingTile.getX() + 3, startingTile.getY() - 2 - i, startingTile.getZ()));
		}
		for (int i = 0; i < 4; i++) {
			positions.add(new Position(startingTile.getX() + 4, startingTile.getY() - 3 - i, startingTile.getZ()));
		}
		for (int i = 0; i < 4; i++) {
			positions.add(new Position(startingTile.getX() + 5, startingTile.getY() - 3 - i, startingTile.getZ()));
		}
		for (int i = 0; i < 3; i++) {
			positions.add(new Position(startingTile.getX() + 5, startingTile.getY() - 4 - i, startingTile.getZ()));
		}
		return positions;
	}

	private List<Position> getNorthEastLightningPositions(Entity target) {
		List<Position> positions = new ArrayList<>();
		Position startingTile = getLightningStartPosition(target);
		for (int i = 0; i < 2; i++) {
			positions.add(new Position(startingTile.getX(), startingTile.getY() + i, startingTile.getZ()));
		}
		for (int i = 0; i < 3; i++) {
			positions.add(new Position(startingTile.getX() + 1, startingTile.getY() + i, startingTile.getZ()));
		}
		for (int i = 0; i < 3; i++) {
			positions.add(new Position(startingTile.getX() + 2, startingTile.getY() + 1 + i, startingTile.getZ()));
		}
		for (int i = 0; i < 4; i++) {
			positions.add(new Position(startingTile.getX() + 3, startingTile.getY() + 2 + i, startingTile.getZ()));
		}
		for (int i = 0; i < 4; i++) {
			positions.add(new Position(startingTile.getX() + 4, startingTile.getY() + 3 + i, startingTile.getZ()));
		}
		for (int i = 0; i < 4; i++) {
			positions.add(new Position(startingTile.getX() + 5, startingTile.getY() + 3 + i, startingTile.getZ()));
		}
		for (int i = 0; i < 3; i++) {
			positions.add(new Position(startingTile.getX() + 5, startingTile.getY() + 4 + i, startingTile.getZ()));
		}
		return positions;
	}

	private List<Position> getNorthWestLightningPositions(Entity target) {
		List<Position> positions = new ArrayList<>();
		Position startingTile = getLightningStartPosition(target);
		for (int i = 0; i < 2; i++) {
			positions.add(new Position(startingTile.getX(), startingTile.getY() + i, startingTile.getZ()));
		}
		for (int i = 0; i < 3; i++) {
			positions.add(new Position(startingTile.getX() - 1, startingTile.getY() + i, startingTile.getZ()));
		}
		for (int i = 0; i < 3; i++) {
			positions.add(new Position(startingTile.getX() - 2, startingTile.getY() + 1 + i, startingTile.getZ()));
		}
		for (int i = 0; i < 4; i++) {
			positions.add(new Position(startingTile.getX() - 3, startingTile.getY() + 2 + i, startingTile.getZ()));
		}
		for (int i = 0; i < 4; i++) {
			positions.add(new Position(startingTile.getX() - 4, startingTile.getY() + 3 + i, startingTile.getZ()));
		}
		for (int i = 0; i < 4; i++) {
			positions.add(new Position(startingTile.getX() - 5, startingTile.getY() + 3 + i, startingTile.getZ()));
		}
		for (int i = 0; i < 3; i++) {
			positions.add(new Position(startingTile.getX() - 5, startingTile.getY() + 4 + i, startingTile.getZ()));
		}
		return positions;
	}

	private Position getClosestStartPoint(Entity target) {
		Position playerPos = target.getPosition().copy();
		Position closestPos = null;
		int closestDistance = Integer.MAX_VALUE;
		for (Position pos : getLightningStartingPositions()) {
			int distance = pos.distance(playerPos);
			if (distance < closestDistance) {
				closestDistance = distance;
				closestPos = pos;
			}
		}

		return closestPos;
	}

	private Position getLightningStartPosition(Entity target) {
		// Get the direction from the boss to the player
		Direction directionToPlayer = Direction.getDirection(npc.getPosition(), target.getPosition());

		boolean nextTileHasClipping = true;
		Position startPos = target.getPosition().copy();
		int distance = getClosestDistanceToStartPoint(target);
		if (startPos.inBounds(eastBounds))
			return new Position(startPos.getX() - distance, startPos.getY(), startPos.getZ());
		if (startPos.inBounds(westBounds))
			return new Position(startPos.getX() + distance, startPos.getY(), startPos.getZ());
		if (startPos.inBounds(southBounds))
			return new Position(startPos.getX(), startPos.getY() + distance, startPos.getZ());
		if (startPos.inBounds(northBounds))
			return new Position(startPos.getX(), startPos.getY() - distance, startPos.getZ());
		if (startPos.inBounds(northEastBounds))
			return new Position(startPos.getX() - distance, startPos.getY() - distance, startPos.getZ());
		if (startPos.inBounds(northWestBounds))
			return new Position(startPos.getX() + distance, startPos.getY() - distance, startPos.getZ());
		if (startPos.inBounds(southEastBounds))
			return new Position(startPos.getX() - distance, startPos.getY() + distance, startPos.getZ());
		if (startPos.inBounds(southWestBounds))
			return new Position(startPos.getX() + distance, startPos.getY() + distance, startPos.getZ());

		return startPos;
	}

	private boolean hasClipping(int x, int y) {
		return Tile.get(x, y, npc.getPosition().getZ(), true).clipping != 0;
	}

	private int getClosestDistanceToStartPoint(Entity target) {
		Position playerPos = target.getPosition().copy();
		int closestDistance = Integer.MAX_VALUE;
		for (Position pos : getLightningStartingPositions()) {
			int distance = pos.distance(playerPos);
			if (distance < closestDistance) {
				closestDistance = distance;
			}
		}
		return closestDistance;
	}

	private List<Position> getLightningPositionsForDirection(Direction patternDirection, Entity target) {
		// Create a map to store the lightning positions for each direction
		Map<Direction, List<Position>> lightningPositions = new HashMap<>();

		// Define the lightning positions for each direction
		lightningPositions.put(Direction.NORTH, getNorthLightningPositions(target));
		lightningPositions.put(Direction.EAST, getEastLightningPositions(target));
		lightningPositions.put(Direction.SOUTH, getSouthLightningPositions(target));
		lightningPositions.put(Direction.WEST, getWestLightningPositions(target));
		lightningPositions.put(Direction.NORTH_EAST, getNorthEastLightningPositions(target));
		lightningPositions.put(Direction.NORTH_WEST, getNorthWestLightningPositions(target));
		lightningPositions.put(Direction.SOUTH_EAST, getSouthEastLightningPositions(target));
		lightningPositions.put(Direction.SOUTH_WEST, getSouthWestLightningPositions(target));

		// Return the lightning positions for the specified direction
		return lightningPositions.get(patternDirection);
	}

	boolean isDiagonalMatch(Direction dir1, Direction dir2) {
		// Check if the two directions are diagonally opposite
		return (dir1 == Direction.NORTH_EAST && dir2 == Direction.SOUTH_WEST) ||
				(dir1 == Direction.NORTH_WEST && dir2 == Direction.SOUTH_EAST) ||
				(dir1 == Direction.SOUTH_EAST && dir2 == Direction.NORTH_WEST) ||
				(dir1 == Direction.SOUTH_WEST && dir2 == Direction.NORTH_EAST);
	}

	boolean isInLineWithPlayer(Position position, Player player) {
		// Check if the position is in line with the player's X or Y coordinate
		return position.getX() == player.getPosition().getX() || position.getY() == player.getPosition().getY();
	}

	List<Position> getLightningStartingPositions() {
		List<Position> positions = new ArrayList<>();
		positions.add(new Position(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 35, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 36, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 38, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 39, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 39, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 36, npc.getPosition().getRegion().baseY + 40, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 40, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 34, npc.getPosition().getRegion().baseY + 40, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 40, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 40, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 40, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 40, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 30, npc.getPosition().getRegion().baseY + 40, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 39, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 38, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 37, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 36, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 35, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 34, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 33, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 30, npc.getPosition().getRegion().baseY + 32, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 32, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 32, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 32, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 34, npc.getPosition().getRegion().baseY + 32, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 32, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 36, npc.getPosition().getRegion().baseY + 32, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 33, 0));
		positions.add(new Position(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 34, 0));
		return positions;
	}

	private void sendRangeOrb() {
		if (target == null)
			return;
		npc.graphics(2484, 0, 0);
		World.startEvent(e -> {
			e.setCancelCondition(() -> npc.getHp() < 1 || targetIsNotInBossRegion());
			int delay = rangeOrb.send(npc, target);
			e.delay(World.getTicks(delay) + 1);
			if (target != null) {
				if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES)) {
					target.hit(new Hit().fixedDamage(0));
				} else {
					damagedPlayer = true;
					target.hit(new Hit().randDamage(25, 40));
				}
			}
		});
	}

	private void sendMageOrb() {
		if (target == null)
			return;
		npc.graphics(2483, 0, 0);
		World.startEvent(e -> {
			e.setCancelCondition(() -> npc.getHp() < 1 || targetIsNotInBossRegion());
			if (target != null) {
				int delay = mageOrb.send(npc, target);
				e.delay(World.getTicks(delay) + 1);
				if (target == null)
					return;

				if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)) {
					target.hit(new Hit().fixedDamage(0));
				} else {
					damagedPlayer = true;
					target.hit(new Hit().randDamage(25, 40));
				}
			}
		});
	}

	private void sendRandomOrb() {
		npc.animate(10281);
		int orb = Random.get(1, totalRallyStyles);
		switch (orb) {
			case 3 -> sendMeleeOrb();
			case 2 -> sendRangeOrb();
			case 1 -> sendMageOrb();
		}
	}
}
