package io.ruin.model.map.route;

import io.ruin.api.utils.Random;
import io.ruin.cache.NPCType;
import io.ruin.cache.LocType;
import io.ruin.model.EntityInteractionMode;
import io.ruin.model.activities.wilderness.Wilderness;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.shared.Movement;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.map.ClipUtils;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.ObjectType;
import io.ruin.model.map.route.routes.DumbRoute;
import io.ruin.model.map.route.routes.ProjectileRoute;
import io.ruin.model.map.route.routes.TargetRoute;
import io.ruin.model.map.route.types.RouteAbsolute;
import io.ruin.model.map.route.types.RouteEntity;
import io.ruin.model.map.route.types.RouteObject;
import io.ruin.model.map.route.types.RouteRelative;

import static io.ruin.model.map.ClipConstants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RouteFinder {

	/**
	 * Separator
	 */

	private Entity entity;

	private ClipUtils clipUtils;

	public ClipUtils getClipUtils() {
		return clipUtils;
	}

	public ClipUtils customClipUtils;

	public RouteFinder(Entity entity) {
		this.entity = entity;
		if (entity.npc != null) {
			NPCType def = entity.npc.getDef();
			if (def.flightClipping) {
				this.clipUtils = ClipUtils.FLIGHT;
				return;
			}
			if (def.swimClipping) {
				this.clipUtils = ClipUtils.SWIM;
				return;
			}
		}
		this.clipUtils = ClipUtils.REGULAR;
	}

	public void setClip(ClipUtils utils) {
		this.clipUtils = utils;
	}

	/**
	 * Moves entity according to given route.
	 */
	public void route(RouteType route, boolean message, boolean ignoreFreeze) {
		route(route, message, ignoreFreeze, entity, customClipUtils != null ? customClipUtils : this.clipUtils);
	}

	public static void route(RouteType route, boolean message, boolean ignoreFreeze, Entity entity, ClipUtils clipUtils) {
		Position position = entity.getPosition();
		int baseX = (entity.getFirstChunkX() - 6) * 8;
		int baseY = (entity.getFirstChunkY() - 6) * 8;
		// Warning, since all RouteFinder classes share 1 ClipUtils class, this function cannot be called concurrently.
		// (Which is fine in our case anyways..)
		clipUtils.update(baseX, baseY, position.getZ());
		Movement movement = entity.getMovement();
		movement.readOffset = 0;
		movement.writeOffset = RouteCalculator.findRoute(entity.getBaseLocalX(), entity.getBaseLocalY(), entity.getSize(),
				route, clipUtils, true, movement.getStepsX(), movement.getStepsY());
		movement.stepType = StepType.NORMAL;
		if (movement.writeOffset == -1) {
			route.finishX = -1;
			route.finishY = -1;
		} else if (movement.writeOffset == 0) {
			route.finishX = position.getX();
			route.finishY = position.getY();
		} else {
			route.finishX = movement.getStepsX()[movement.writeOffset - 1];
			route.finishY = movement.getStepsY()[movement.writeOffset - 1];
		}
		if (entity.isMovementBlocked(message, ignoreFreeze))
			entity.getMovement().reset();
	}

	/**
	 * Route absolute (Attempts to move exactly to the given coordinates)
	 */

	private RouteAbsolute routeAbsolute;

	public RouteType routeAbsolute(int destX, int destY, boolean message, boolean skipChecks) {
		if (routeAbsolute == null)
			routeAbsolute = new RouteAbsolute();
		routeAbsolute.set(destX, destY);
		route(routeAbsolute, message, skipChecks);
		return routeAbsolute;
	}

	public RouteType routeAbsolute(int destX, int destY) {
		return routeAbsolute(destX, destY, false);
	}

	public RouteType routeAbsolute(int destX, int destY, boolean message) {
		if (routeAbsolute == null)
			routeAbsolute = new RouteAbsolute();
		routeAbsolute.set(destX, destY);
		route(routeAbsolute, message, false);
		return routeAbsolute;
	}

	/**
	 * Relative route (Attempts to move to the given coordinates based on size)
	 */

	private RouteRelative routeRelative;

	public RouteType routeRelative(int destX, int destY) {
		if (routeRelative == null)
			routeRelative = new RouteRelative();
		routeRelative.set(destX, destY, entity.getSize(), entity.getSize(), 0);
		route(routeRelative, true, false);
		return routeRelative;
	}

	public RouteType getRelativeRouteType(int destX, int destY) {
		if (routeRelative == null)
			routeRelative = new RouteRelative();
		routeRelative.set(destX, destY, entity.getSize(), entity.getSize(), 0);
		return routeRelative;
	}

	/**
	 * Route ground item
	 */

	public void routeGroundItem(GroundItem groundItem, Consumer<Integer> successConsumer) {
		if (groundItem == null) {
			return;
		}
		RouteType route = routeAbsolute(groundItem.getX(), groundItem.getY());
		// todo - make this use route relavite when the obj is on something !
		// RouteType route = routeRelative(groundItem.x, groundItem.y);
		entity.startEvent(event -> {
			while (!entity.getMovement().isAtDestination()) {
				if (groundItem.tile == null) {
					entity.face(groundItem.getX(), groundItem.getY());
					entity.getMovement().reset();
					return;
				}
				event.delay(1);
			}
			if (groundItem.tile == null) {
				entity.face(groundItem.getX(), groundItem.getY());
				return;
			}
			if (route.finished(entity.getPosition())) {
				if (route.reachable) {
					successConsumer.accept(0);
					return;
				}
				Position pos = entity.getPosition();
				int diffX = pos.getX() - groundItem.getX();
				int diffY = pos.getY() - groundItem.getY();
				int mask = getDirectionMask(diffX, diffY);
				if (mask != 0) {
					Tile tile = Tile.get(pos.getX(), pos.getY(), pos.getZ());
					if (tile == null || tile.allowEntrance(mask)) {
						entity.face(groundItem.getX(), groundItem.getY());
						event.delay(1); // trying to match osrs as much as possible
						successConsumer.accept(1);
						return;
					}
				}
			}
			entity.face(groundItem.getX(), groundItem.getY());
			if (entity.player != null)
				entity.player.getMovement().outOfReach();
		}).ignoreCombatReset();
	}

	/**
	 * Route object
	 */

	private RouteObject routeObject;

	public RouteObject routeObject(GameObject gameObject) {
		if (routeObject == null)
			routeObject = new RouteObject();
		LocType definition = gameObject.getDef();
		if (gameObject.getType() == 10 || gameObject.getType() == 11 || gameObject.getType() == 22) {
			int xLength, yLength;
			if (gameObject.getDirection() == 0 || gameObject.getDirection() == 2) {
				xLength = definition.xLength;
				yLength = definition.yLength;
			} else {
				xLength = definition.yLength;
				yLength = definition.xLength;
			}
			int someDirection = gameObject.getDef().someDirection;
			if (gameObject.getDirection() != 0)
				someDirection =
						(0xf & someDirection << gameObject.getDirection()) + (someDirection >> -gameObject.getDirection() + 4);
			routeObject.set(gameObject.x, gameObject.y, xLength, yLength, ObjectType.values()[gameObject.getType()],
					gameObject.getDirection(), someDirection);
		} else if (gameObject.getType() >= 0 && gameObject.getType() <= 3 || gameObject.getType() == 9) {
			routeObject.set(gameObject.x, gameObject.y, 0, 0, ObjectType.values()[gameObject.getType()],
					gameObject.getDirection(), null);
		} else {
			routeObject.set(gameObject.x, gameObject.y, 0, 0, ObjectType.values()[gameObject.getType()],
					gameObject.getDirection(), null);
		}
		route(routeObject, false, false);
		return routeObject;
	}

	public void routeObject(GameObject gameObject, Runnable successAction) {
		RouteType route;
		if (gameObject.walkTo != null)
			route = routeAbsolute(gameObject.walkTo.getX(), gameObject.walkTo.getY());
		else
			route = routeObject(gameObject);
		if (entity.player != null) {
			if (entity.player.emoteDelay.isDelayed()) {
				entity.player.resetAnimation();
				entity.player.emoteDelay.reset();
			}
		}
		/**
		 * No event required, already at destination.
		 */
		if (route.finished(entity.getPosition())) {
			entity.face(gameObject);
			if (route.reachable) {
				successAction.run();
				return;
			}
			if (gameObject.skipReachCheck != null && gameObject.skipReachCheck.test(entity.getPosition())
					|| gameObject.getId() == 56246) {
				successAction.run();
				return;
			}
			if (entity.player != null)
				entity.player.getMovement().outOfReach();
			return;
		}
		/**
		 * Event required, not yet at destination.
		 */
		int id = gameObject.getId();
		entity.startEvent(event -> {
			while (!entity.getMovement().isAtDestination()) {
				if (gameObject.getId() != id || gameObject.tile == null) {
					entity.face(gameObject);
					entity.getMovement().reset();
					return;
				}
				event.delay(1);
			}
			entity.face(gameObject);
			if (gameObject.getId() != id || gameObject.tile == null) {
				/* obj was changed or removed */
				return;
			}
			if (route.reachable) {
				if (route.finished(entity.getPosition())) {
					successAction.run();
					return;
				}
			}
			if ((gameObject.skipReachCheck != null && gameObject.skipReachCheck.test(entity.getPosition())) ||
					(EntityInteractionMode.isAPObject(gameObject) && ProjectileRoute.allow(entity, gameObject.getPosition()))) {
				successAction.run();
				return;
			}
			if (entity.player != null)
				entity.player.getMovement().outOfReach();
		}).ignoreCombatReset();
	}

	/**
	 * Route entity
	 */

	public RouteEntity routeEntity;

	public RouteEntity routeSelf() {
		return routeEntity(entity);
	}

	public RouteEntity routeSelf(boolean ignoreFreeze) {
		return routeEntity(entity, ignoreFreeze);
	}

	public RouteEntity routeEntity(Entity target) {
		routeTheEntity(target);
		route(routeEntity, false, false);
		return routeEntity;
	}

	public RouteEntity routeEntity(Entity target, boolean ignoreFreeze) {
		routeTheEntity(target);
		route(routeEntity, false, ignoreFreeze);
		return routeEntity;
	}

	private void routeTheEntity(Entity target) {
		Position targetPos = target.getPosition();
		int x = targetPos.getX();
		int y = targetPos.getY();
		int size = target.getSize();
		if (routeEntity == null)
			routeEntity = new RouteEntity();
		if (entity.player != null) {
			if (entity.player.emoteDelay.isDelayed()) {
				entity.player.resetAnimation();
				entity.player.emoteDelay.reset();
			}
		}
		routeEntity.set(x, y, size, size, 0);
	}

	/**
	 * Misc routes stuff
	 */

	public TargetRoute targetRoute;

	/**
	 * Step check
	 */

	public int allowStep(int stepX, int stepY) {
		if (targetRoute != null && !targetRoute.allowStep(entity, stepX, stepY)) {
			entity.getMovement().reset();
			return 1;
		}
		if (entity.getMovement().stepType == StepType.NORMAL) {
			if (entity.player != null) {
				if (Wilderness.stopFollowing(entity.player, stepX, stepY))
					return 2;
			} else if (!entity.npc.getDef().ignoreOccupiedTiles && Tile.isOccupied(entity, stepX, stepY)) {
				entity.getMovement().reset(); // let's reset the movement so the occupied check isn't spammed (Unless ofc it's
																			// combat, it will be spammed regardless)
				return 3;
			}
			if (DumbRoute.getDirection(entity.getRouteFinder().getClipUtils(), entity.getAbsX(), entity.getAbsY(),
					entity.getHeight(), entity.getSize(), stepX, stepY) == null) {
				// movement stays "queued" as long as you stay still.
				return 4;
			}
		}
		return 0;
	}

	/**
	 * Derp
	 */

	public static int getDirectionMask(int diffX, int diffY) {
		if (diffX == -1) {
			if (diffY == -1)
				return SOUTH_WEST_MASK;
			if (diffY == 1)
				return NORTH_WEST_MASK;
			return WEST_MASK;
		}
		if (diffX == 1) {
			if (diffY == -1)
				return SOUTH_EAST_MASK;
			if (diffY == 1)
				return NORTH_EAST_MASK;
			return EAST_MASK;
		}
		if (diffY == -1)
			return SOUTH_MASK;
		if (diffY == 1)
			return NORTH_MASK;
		return 0; // invalid diffs
	}

	/**
	 * Walkable stuff
	 */

	public static Position findWalkable(Position fromPos) {
		return findWalkable(fromPos.getX(), fromPos.getY(), fromPos.getZ());
	}

	public static Position findWalkable(int fromX, int fromY, int fromZ, int minDistance, int radius) {
		List<Position> positions = new ArrayList<>();

		for (int x = (fromX - radius); x <= (fromX + radius); x++) {
			for (int y = (fromY - radius); y <= (fromY + radius); y++) {
				if (Math.abs(x - fromX) < minDistance && Math.abs(y - fromY) < minDistance) {
					continue;
				}

				Tile tile = Tile.get(x, y, fromZ);
				if (tile != null && !tile.allowStandardEntrance()) {
					continue;
				}

				positions.add(new Position(x, y, fromZ));
			}
		}

		return positions.isEmpty() ? null : Random.get(positions);
	}

	public static Position findWalkable(int fromX, int fromY, int fromZ) {
		int radius = 1; // Extending this would require much heavier checks.
		List<Position> positions = new ArrayList<>(radius * 8);
		for (int x = (fromX - radius); x <= (fromX + radius); x++) {
			for (int y = (fromY - radius); y <= (fromY + radius); y++) {
				if (x == fromX && y == fromY) {
					/* exclude this tile! */
					continue;
				}
				Tile tile = Tile.get(x, y, fromZ);
				if (tile != null && !tile.allowStandardEntrance()) {
					/* tile can't be entered! */
					continue;
				}
				positions.add(new Position(x, y, fromZ));
			}
		}
		return Random.get(positions);
	}

	public RouteType routeAbsolute(Position toDestination) {
		return routeAbsolute(toDestination.getX(), toDestination.getY(), false);
	}

}
