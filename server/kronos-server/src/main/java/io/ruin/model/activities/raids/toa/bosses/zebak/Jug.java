package io.ruin.model.activities.raids.toa.bosses.zebak;

import io.ruin.model.World;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;

import java.util.List;

public class Jug extends NPC {
	List<Position> rockPositions;

	public Jug(int id, List<Position> rockPositions) {
		super(id);
		this.rockPositions = rockPositions;
	}

	public static void pullJug(Player player, NPC npc) {
		Position tile = new Position(npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ());
		Direction playerFacingDirection = Direction.getDirection(player.getPosition(), tile);
		if (playerFacingDirection == null)
			return;
		player.face(playerFacingDirection);
		int pullDistance = 1;
		int newX = tile.getX() - playerFacingDirection.deltaX * pullDistance;
		int newY = tile.getY() - playerFacingDirection.deltaY * pullDistance;
		Position newPosition = new Position(newX, newY, tile.getZ());
		Bounds bounds = new Bounds(
			npc.getPosition().getRegion().baseX + 23,
			npc.getPosition().getRegion().baseY + 22,
			npc.getPosition().getRegion().baseX + 37,
			npc.getPosition().getRegion().baseY + 40,
			npc.getPosition().getZ()
		);
		if (newPosition.inBounds(bounds)) {
			npc.transform(11736);
			npc.stepAbs(newX, newY, StepType.WALK);
			World.startEvent(e -> {
				e.delay(2);
				if (npc != null) {
					if (player.getCurrentToARaid() != null)
						player.getCurrentToARaid().jugRolls++;
					npc.transform(11735);
				}
			});
		} else {
			Position adjustedPosition = Bounds.clamp(newPosition, bounds);
			if (adjustedPosition.getX() > bounds.neX)
				adjustedPosition = new Position(bounds.neX, adjustedPosition.getY(), adjustedPosition.getZ());
			if (adjustedPosition.getX() < bounds.swX)
				adjustedPosition = new Position(bounds.swX, adjustedPosition.getY(), adjustedPosition.getZ());
			if (adjustedPosition.getY() > bounds.neY)
				adjustedPosition = new Position(adjustedPosition.getX(), bounds.neY, adjustedPosition.getZ());
			if (adjustedPosition.getY() < bounds.swY)
				adjustedPosition = new Position(adjustedPosition.getX(), bounds.swY, adjustedPosition.getZ());

			int distance = npc.getPosition().distance(adjustedPosition);
			Position playerAdjustedPosition = Bounds.clamp(new Position(player.getPosition().getX() - playerFacingDirection.deltaX * pullDistance, player.getPosition().getY() - playerFacingDirection.deltaY * pullDistance, player.getPosition().getZ()), bounds);
			npc.transform(11736);
			npc.stepAbs(adjustedPosition.getX(), adjustedPosition.getY(), StepType.WALK);
			player.stepAbs(playerAdjustedPosition.getX(), playerAdjustedPosition.getY(), StepType.WALK);
			World.startEvent(e -> {
				e.delay(distance + 1);
				if (npc != null) {
					if (player.getCurrentToARaid() != null)
						player.getCurrentToARaid().jugRolls++;
					npc.transform(11735);
				}
			});
		}
	}


	public static void pushJug(Player player, NPC npc) {
		Bounds bounds = new Bounds(
			npc.getPosition().getRegion().baseX + 23,
			npc.getPosition().getRegion().baseY + 22,
			npc.getPosition().getRegion().baseX + 37,
			npc.getPosition().getRegion().baseY + 40,
			npc.getPosition().getZ()
		);
		if (player.getPosition().distance(npc.getPosition()) > 1) {
			Position nearestTile = null;
			for (int j = -1; j < 2; j++) {
				for (int i = -1; i < 2; i++) {
					if ((i == 0 || j == 0) && (i != 0 || j != 0)) {
						Position tile = new Position(npc.getPosition().getX() + i, npc.getPosition().getY() + j, npc.getPosition().getZ());
						if (nearestTile == null || player.getPosition().distance(nearestTile) > player.getPosition().distance(tile)) {
							nearestTile = tile;
						}
					}
				}
			}
			Position finalNearestTile = nearestTile;
			player.startEvent(e -> {
				player.getRouteFinder().routeAbsolute(finalNearestTile.getX(), finalNearestTile.getY());
				e.waitForMovement(player);
				pushJug(player, npc);
			});
			return;
		}
		Direction playerFacingDirection = Direction.getDirection(player.getPosition(), npc.getPosition());
		if (playerFacingDirection == null)
			return;
		player.face(playerFacingDirection);
		int pushDistance = 7;

		int newX = npc.getPosition().getX() + playerFacingDirection.deltaX * pushDistance;
		int newY = npc.getPosition().getY() + playerFacingDirection.deltaY * pushDistance;

		Position newPosition = new Position(newX, newY, npc.getPosition().getZ());

		// Check if the new position is within bounds
		if (newPosition.inBounds(bounds)) {
			npc.transform(11736);
			npc.stepAbs(newX, newY, StepType.WALK);
			World.startEvent(e -> {
				e.delay(8);
				if (npc != null) {
					if (player.getCurrentToARaid() != null)
						player.getCurrentToARaid().jugRolls++;
					npc.transform(11735);
				}
			});
		} else {
			// Adjust the position to stay within bounds
			Position adjustedPosition = Bounds.clamp(newPosition, bounds);
			if (adjustedPosition.getX() > bounds.neX)
				adjustedPosition = new Position(bounds.neX, adjustedPosition.getY(), adjustedPosition.getZ());
			if (adjustedPosition.getX() < bounds.swX)
				adjustedPosition = new Position(bounds.swX, adjustedPosition.getY(), adjustedPosition.getZ());
			if (adjustedPosition.getY() > bounds.neY)
				adjustedPosition = new Position(adjustedPosition.getX(), bounds.neY, adjustedPosition.getZ());
			if (adjustedPosition.getY() < bounds.swY)
				adjustedPosition = new Position(adjustedPosition.getX(), bounds.swY, adjustedPosition.getZ());

			int distance = npc.getPosition().distance(adjustedPosition);

			npc.transform(11736);
			npc.stepAbs(adjustedPosition.getX(), adjustedPosition.getY(), StepType.WALK);
			World.startEvent(e -> {
				e.delay(distance + 1);
				if (npc != null) {
					if (player.getCurrentToARaid() != null)
						player.getCurrentToARaid().jugRolls++;
					npc.transform(11735);
				}
			});
		}
	}


}
