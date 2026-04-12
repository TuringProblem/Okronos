package mokhaiotl.combat.attacks.impl;

import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.*;
import mokhaiotl.combat.MokhaiotlCombat;
import mokhaiotl.combat.attacks.Attack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-30
 */
public class BurrowedSlamAttack implements Attack {
	private final int SHOCKWAVE_RINGS = 16;

	@Override
	public void invoke(Player target, NPCCombat mokhaiotl) {
		World.startEvent(event -> {
			// get the center of the boss, and ring by ring performs a slam
			mokhaiotl.getNpc().animate(12419);
			shockArena(mokhaiotl.getNpc().getCentrePosition());
			event.delay(1);
			mokhaiotl.getNpc().animate(12419);
			((MokhaiotlCombat) mokhaiotl).resetBurrowed();
		});
	}

	/**
	 * Executes a shockwave effect in the arena. This method removes specific game objects
	 * within the affected area and applies graphical effects, progressing outward in rings
	 * from the specified center position.
	 *
	 * @param centrePosition the central position from which the shockwave effect originates
	 */
	private void shockArena(Position centrePosition) {
		var dangerTiles = new HashSet<Position>();
		var shadowPositions = new HashSet<Position>();
		for (int i = 0; i <= SHOCKWAVE_RINGS; i++) {
			var ringPositions = getRingPositions(centrePosition, i);
			for (var pos : ringPositions) {
				var tile = Tile.get(pos);
				if (tile != null && tile.gameObjects != null && !tile.gameObjects.isEmpty()) {
					var object = tile.gameObjects.getFirst();
					if (object.getId() == 57286) {
						shadowPositions.addAll(calculateShadowPositions(centrePosition, pos));
						object.remove();
					}
					if (shadowPositions.contains(pos)) continue;
					dangerTiles.add(pos);
				}
			}
		}
		lightUpTiles(centrePosition.getRegion(), dangerTiles);
	}

	private void lightUpTiles(Region region, HashSet<Position> dangerTiles) {
		dangerTiles.forEach(pos -> {
			World.sendGraphics(3405, 0, 0, pos);
			region.players.forEach(player -> {
				if (player.getPosition().isAtPosition(pos))
					player.hit(new Hit().randDamage(50).ignoreDefence().ignorePrayer());
			});
		});
	}

	private List<Position> getRingPositions(Position center, int ringDistance) {
		var positions = new ArrayList<Position>();
		if (ringDistance == 0) {
			positions.add(center);
			return positions;
		}
		int centerX = center.getX();
		int centerY = center.getY();
		int z = center.getZ();
		for (int x = centerX - ringDistance; x <= centerX + ringDistance; x++) {
			for (int y = centerY - ringDistance; y <= centerY + ringDistance; y++) {
				if (Math.abs(x - centerX) == ringDistance || Math.abs(y - centerY) == ringDistance) {
					positions.add(new Position(x, y, z));
				}
			}
		}
		return positions;
	}

	private Set<Position> calculateShadowPositions(Position centrePosition, Position boulderPosition) {
		var shadowPositions = new HashSet<Position>();

		int deltaX = boulderPosition.getX() - centrePosition.getX();
		int deltaY = boulderPosition.getY() - centrePosition.getY();

		var direction = Direction.getDirection(deltaX, deltaY);
		var distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

		// Boulder is at center, no shadow
		if (distance == 0)
			return shadowPositions;

		var shadowEndPosition = Position.of(
			(boulderPosition.getX() + (direction.deltaX * 4)),
			(boulderPosition.getY() + (direction.deltaY * 4)),
			boulderPosition.getZ()
		);

		// draw a line from the boulderPosition to the shadowEndPosition
		var shadowBounds = new Bounds(
			boulderPosition.x(),
			boulderPosition.y(),
			shadowEndPosition.x(),
			shadowEndPosition.y()
		);

//		shadowPositions.addAll(shadowBounds.getAllPositions());

		return shadowPositions;
	}
}
