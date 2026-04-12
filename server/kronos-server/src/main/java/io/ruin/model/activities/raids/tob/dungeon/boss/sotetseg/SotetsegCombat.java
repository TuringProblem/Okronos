package io.ruin.model.activities.raids.tob.dungeon.boss.sotetseg;

import io.ruin.Server;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.TheSoulsplit;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.route.routes.DumbRoute;
import io.ruin.model.skills.prayer.Prayer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class SotetsegCombat extends NPCCombat {


	private int stage = 0;
	private static final int OFFSET = 255;
	public static final int RANGED_ANIM = 8139;


	public boolean damagedPlayer = false;

	private static final Projectile RED_CUBE = new Projectile(1606, 35, 30, 1, 46, 8, 0, OFFSET).regionBased();
	private static final Projectile BLACK_CUBE = new Projectile(1607, 35, 30, 0, 46, 8, 0, OFFSET).regionBased();
	private static final Projectile BLUE_CUBE = new Projectile(1609, 35, 30, 0, 46, 8, 0, 50).regionBased();
	private static final Projectile RED_BALL = new Projectile(1604, 35, 30, 1, 130, 8, 0, 150).regionBased();
	private static final int RED_BALL_SPLASH = 1605;


	boolean canAttack = true;

	Bounds mazeTileBounds;
	Bounds theatreCombatBounds;

	@Override
	public void init() {
		mazeTileBounds = new Bounds(npc.getPosition().getRegion().baseX + 9, npc.getPosition().getRegion().baseY + 22, npc.getPosition().getRegion().baseX + 22, npc.getPosition().getRegion().baseY + 36, npc.getHeight());
		theatreCombatBounds = new Bounds(npc.getPosition().getRegion().baseX + 9, npc.getPosition().getRegion().baseY + 20, npc.getPosition().getRegion().baseX + 22, npc.getPosition().getRegion().baseY + 45, npc.getHeight());
		npc.hitListener = new HitListener().postDamage(hit -> {
			if (hit.attacker != null && hit.attacker.isPlayer()) {
				hit.attacker.player.tobDamageDealt += hit.damage;
			}

			double ratio = ((double) npc.getHp() / npc.getMaxHp());
			if (ratio <= .70 && stage == 0 && mazes == 0 && !inMaze)
				startMazeEvent();
			else if (ratio <= .50 && stage == 1 && mazes == 1 && !inMaze)
				startMazeEvent();
			else if (ratio <= .3 && stage == 2 && mazes == 2 && !inMaze)
				startMazeEvent();
		});
	}

	@Override
	public void follow() {

	}


	@Override
	public boolean attack() {
		if (canAttack && !inMaze) {
			// if (Random.rollPercent(10) && npc.localPlayers().size() > 1) {
			//  smallRedBall(target);
			// } else {
			autoAttack();
			// }
		}
		return true;
	}

	public boolean wearingBarrows = true;

	@Override
	public void process() {
		if (!npc.getPosition().getRegion().players.isEmpty()) {
			for (Player p : npc.getPosition().getRegion().players) {
				if (!p.wearingBarrows())
					wearingBarrows = false;
			}
		}
	}


	private void autoAttack() {
		npc.animate(RANGED_ANIM);
		npc.localPlayers().forEach(p -> {
			if (p.currentParty != null && !p.currentParty.deadPlayers.contains(p)) {
				int maxDamage = 47;
				if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
					maxDamage = 9;
				else damagedPlayer = true;

				int delay = BLUE_CUBE.send(npc, p);
				Hit hit = new Hit(npc, AttackStyle.MAGIC).randDamage(maxDamage).clientDelay(delay);
				p.hit(hit);
			}
		});
	}


	private int getTicks(int delay) {
		return Math.max(1, (delay * 16) / Server.tickMs());
	}


	private void smallRedBall(Entity target) {
		AtomicInteger damage = new AtomicInteger(30);
		World.startEvent(e -> {
			target.player.sendMessage("You have been chosen!");
			npc.animate(RANGED_ANIM);
			int delay = RED_CUBE.send(npc, target);
			e.delay(getTicks(delay));
			target.localPlayers().forEach(p -> {
				if (p == target)
					return;
				if (p.getPosition().distance(target.getPosition()) > 0)
					damage.addAndGet(25);
			});
			Hit hit = new Hit(npc, AttackStyle.MAGICAL_MELEE).fixedDamage(damage.get()).ignorePrayer();
			target.hit(hit);
		});
	}

	private void smallBall(Entity target) {
		npc.animate(RANGED_ANIM);
		int maxDamage = 47;

		Player closest = null;
		double closestDistance = Double.MAX_VALUE;
		for (Player p : npc.getPosition().getRegion().players) {
			if (p.currentParty != null && p.currentParty.deadPlayers.contains(p))
				continue;
			double distance = npc.getPosition().distance(p.getPosition());
			if (distance < closestDistance) {
				closest = p;
				closestDistance = distance;
			}
		}
		if (closest != null) {
			int delay = RED_CUBE.send(npc, target);
			npc.startEvent(event -> event.delay(3));
			Hit hit = new Hit(npc, AttackStyle.MAGIC).randDamage(maxDamage).ignorePrayer().clientDelay(delay);
			Player newTarget2 = null;
			Player newTarget;
			target.hit(hit);
			if (!target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)) {
				target.player.getPrayer().slashPrayers();
				target.player.getPrayer().slashDelay.delay(5);
			}
			if (npc.getPosition().getRegion().players.size() > 1) {
				List<Player> newTargets = new ArrayList<>();
				for (Player player : npc.getPosition().getRegion().players) {
					if (player.currentParty != null && player.currentParty.deadPlayers.contains(player))
						continue;
					newTargets.add(player);
				}
				newTargets.remove(target.player);
				newTarget = newTargets.get(Random.get(newTargets.size() - 1));
				newTarget.remove(newTarget);
				if (newTargets.size() > 0) {
					newTarget2 = newTargets.get(Random.get(newTargets.size() - 1));
				}

				int delay2 = BLACK_CUBE.send(target, newTarget);
				int delay3 = BLUE_CUBE.send(target, newTarget2);
				npc.startEvent(event -> event.delay(3));
				Hit hit2 = new Hit(npc, AttackStyle.RANGED).randDamage(maxDamage).clientDelay(delay2).ignorePrayer();
				Hit hit3 = new Hit(npc, AttackStyle.RANGED).randDamage(maxDamage).clientDelay(delay3).ignorePrayer();
				newTarget.hit(hit2);
				newTarget2.hit(hit3);
				if (!newTarget.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES)) {
					newTarget.player.getPrayer().slashPrayers();
					newTarget.player.getPrayer().slashDelay.delay(5);
				}
				if (!newTarget2.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES)) {
					newTarget2.player.getPrayer().slashPrayers();
					newTarget2.player.getPrayer().slashDelay.delay(5);
				}
			}
		}
	}


	private void knockback(Player target) {
		if (target == null) return;
		// Desired Y position based on the region's base Y plus 20
		int targetY = target.getPosition().getRegion().baseY + 20;

		int endX = target.getAbsX();
		int endY = target.getAbsY();
		int newY = target.getAbsY();

		for (int i = 0; i < 4; i++) {
			// Check if the target Y has been reached or exceeded
			if (newY >= targetY) {
				endY = targetY; // Set the Y to the target Y position
				break;
			}
			if (DumbRoute.getDirection(endX, newY, npc.getHeight(), target.getSize(), endX, newY + 1) != null)
				newY += 1; // Move towards the target Y position
			else
				break; // Cannot move further
		}

		target.animate(1157);
		target.graphics(245, 5, 124);
		target.hit(new Hit().fixedDamage(20));
		target.stun(2, true);

		if (endY != target.getAbsY() && target.player != null)
			target.player.getMovement().teleport(endX, endY, npc.getHeight());
		else
			target.getCombat().reset();

		target.player.sendMessage("Sotetseg, knocks you back!");
		stage++;
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

	int mazes = 0;

	private void tornadoBleedEvent(Player player) {
		World.startEvent(e -> {
			e.setCancelCondition(() -> player.getHp() < 1 || npc.getHp() < 1);
			for (int i = 0; i < 10; i++) {
				player.hit(new Hit().randDamage(8, 19));
				e.delay(3);
			}
		});
	}

	private void startMazeEvent() {
		npc.animate(8138);
		if (inMaze)
			return;
		List<Position> path = generatePath();
		List<Position> dangerTiles = new ArrayList<>();
		inMaze = true;
		mazes++;
		int mazesCompleted = mazes;
		tornadoAtDestination = false;
		// Determine danger tiles (tiles not on the path)
		mazeTileBounds.forEachPos(pos -> {
			boolean isPath = path.stream().anyMatch(pathTile -> pos.distance(pathTile) < 1);
			if (!isPath) {
				dangerTiles.add(pos);
			}
		});

		// Collect performing players and knock them all back to the otherside of the maze
		var playersInScope = npc.getPosition().getRegion().players
			.stream().filter(p -> theatreCombatBounds.inBounds(p))
			.toList();

		/*
		 * An event that will run the same timeframe as the tornado
		 * this will check the danger tiles and the player's position
		 * if they match, deal some moderate damage
		 */
		World.startEvent(e -> {
			while(!dangerTiles.isEmpty()) {
				dangerTiles.forEach(position -> playersInScope.stream()
					.filter(p -> p.getPosition().isAtPosition(position))
					.forEach(p -> {
						damagedPlayer = true;
						p.hit(new Hit().fixedDamage(20));
					}));
				e.delay(1);
			}
		}).setCancelCondition(() -> mazes != mazesCompleted || npc.getHp() < 1);

		World.startEvent(e -> {
			e.setCancelCondition(() -> mazes != mazesCompleted || npc.getHp() < 1);
			NPC tornado = null;
			// If the path is empty, exit the event
			if (path.isEmpty()) return;
			// Run the event for a specified duration (18 ticks in this case)
			for (int i = 0; i < 50; i++) {
				if (i == 12) {
					tornado = new NPC(8389).spawn(path.getFirst());
					handleTornadoMovementEvent(tornado, path);
				}
				if (tornado != null) {
					for (Player player : npc.getPosition().getRegion().players) {
						if (player.getPosition().distance(tornado.getPosition()) < 1) {
							player.hit(new Hit().randDamage(25, 41));
							player.sendMessage(Color.RED.wrap("You have been inflicted by a bad bleed from taking damage from the tornado!"));
							tornadoBleedEvent(player);
							damagedPlayer = true;
						}
					}
				}
				if (i > 3)
					path.forEach(pos -> World.sendGraphics(1605, 0, 0, pos));

				e.delay(1); // Delay to simulate movement over time
				if (i == 49 || tornadoAtDestination || npc.getHp() < 1) {
					inMaze = false;
					path.forEach(pos -> World.sendGraphics(-1, 0, 0, pos));
					if (tornado != null && !tornado.isRemoved())
						tornado.remove();
					dangerTiles.clear();
					break;
				}
			}
		});

		playersInScope.forEach(this::knockback);

		World.startEvent(e -> {
			npc.getPosition().getRegion().players.forEach(p ->
				p.sendMessage("You have 5 seconds to start the maze before the floor consumes you..."));
			e.delay(11);
			npc.getPosition().getRegion().players.forEach(p -> {
				if (p.getPosition().inBounds(mazeTileBounds))
					return;
				p.sendMessage("The floor consumes you.");
				if (npc.getHp() > 0)
					p.hit(new Hit().fixedDamage(p.getHp()));
			});
		});
	}

	boolean inMaze = false;
	boolean tornadoAtDestination = false;

	private void handleTornadoMovementEvent(NPC tornado, List<Position> pathPositions) {
		tornadoAtDestination = false;
		tornado.addEvent(e -> {
			for (Position nextPosition : pathPositions) {
				tornado.stepAbs(nextPosition.getX(), nextPosition.getY(), StepType.WALK);
				e.waitForMovement(tornado); // Wait until the tornado reaches the position
				//if last index
				if (pathPositions.indexOf(nextPosition) == pathPositions.size() - 1) {
					tornado.remove();
					tornadoAtDestination = true;
					inMaze = false;
				}
			}
		});
	}


	private List<Position> generatePath() {
		List<Position> path = new ArrayList<>();
		Set<Position> visited = new HashSet<>();

		// Start at the bottom middle of the maze area
		int startX = (mazeTileBounds.swX + mazeTileBounds.neX) / 2;
		int startY = mazeTileBounds.swY;
		Position start = new Position(startX, startY, npc.getHeight());
		path.add(start);
		visited.add(start);

		Position previous = start; // Keep track of the previous tile visited

		// Continue until the path reaches the top and is at least 30 tiles long
		while (path.size() < 30 || path.get(path.size() - 1).getY() < mazeTileBounds.neY) {
			if (path.size() - 1 < 0) {
				break;
			}
			Position current = path.get(path.size() - 1);
			List<Position> neighbors = getForwardAndSidewaysNeighbors(current, visited, previous, path);
			neighbors.removeAll(visited); // Remove already visited tiles from consideration
			if (!neighbors.isEmpty()) {
				Position next = Random.get(neighbors);
				path.add(next);
				visited.add(next);
				previous = current; // Update the previous tile visited
			} else {
				// Backtrack if no unvisited forward or sideways neighbors available
				path.remove(path.size() - 1);
				if (!path.isEmpty()) {
					previous = path.get(path.size() - 1); // Update the previous tile visited
				}
			}
		}
		return path;
	}

	private List<Position> getForwardAndSidewaysNeighbors(Position position, Set<Position> visited, Position previous, List<Position> path) {
		List<Position> neighbors = new ArrayList<>();

		// Possible moves: right, left, up
		int[][] moves = {{1, 0}, {-1, 0}, {0, 1}}; // Exclude moving down (-1, 0)

		for (int[] move : moves) {
			int newX = position.getX() + move[0];
			int newY = position.getY() + move[1];

			Position newPosition = new Position(newX, newY, position.getZ());
			if (isValidMove(newPosition) && !visited.contains(newPosition) && !newPosition.equals(previous)) {
				// Check if the next tile is already in the path
				if (!path.contains(newPosition)) {
					neighbors.add(newPosition);
				}
			}
		}
		return neighbors;
	}


	private List<Position> getNeighbors(Position position, Set<Position> visited) {
		List<Position> neighbors = new ArrayList<>();

		// Possible moves: right, left, up, down
		int[][] moves = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

		for (int[] move : moves) {
			int newX = position.getX() + move[0];
			int newY = position.getY() + move[1];

			Position newPosition = new Position(newX, newY, position.getZ());
			if (isValidMove(newPosition) && !visited.contains(newPosition)) {
				neighbors.add(newPosition);
			}
		}
		return neighbors;
	}

	private boolean isValidMove(Position position) {
		// Check if the move is within the maze boundaries
		return position.getX() >= mazeTileBounds.swX && position.getX() <= mazeTileBounds.neX
			&& position.getY() >= mazeTileBounds.swY && position.getY() <= mazeTileBounds.neY;
	}

	private SotetsegNPC asSotetseg() {
		return (SotetsegNPC) npc;
	}
}
