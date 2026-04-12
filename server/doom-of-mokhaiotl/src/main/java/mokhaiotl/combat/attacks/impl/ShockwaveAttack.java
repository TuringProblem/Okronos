package mokhaiotl.combat.attacks.impl;

import core.api.Random;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.*;
import mokhaiotl.combat.attacks.Attack;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-30
 */
public class ShockwaveAttack implements Attack {

	private final List<NPC> volatileEarchList = new ArrayList<>();

	@Override
	public void invoke(Player target, NPCCombat mokhaiotl) {
		var delveLevel = target.get("MOKHAIOTL_DELVE_LEVEL", 1);
		// grab 25 tiles from around the arena
		var tiles = getArenaPositions(mokhaiotl);
		var plumes = new ArrayList<Position>();
		for (int i = 0; i < 25; i++) {
			var selected = Random.get(tiles);
			plumes.add(selected);
			tiles.remove(selected);
		}
		// loop over each position and spawn in a Volatile Plume
		plumes.forEach(pos -> {
			var volatileEarth = new NPC(14714).spawn(pos);
			if (Objects.nonNull(target.currentDynamicMap))
				target.currentDynamicMap.addNpc(volatileEarth);
			volatileEarchList.add(volatileEarth);
		});
		// kick off a 19-tick delay before slaming the ground
		World.startEvent(19, event -> {
			mokhaiotl.getNpc().animate(12412);
			volatileEarchList.forEach(NPC::remove);
			event.delay(1);
			mokhaiotl.getNpc().animate(12413);
			event.delay(1);
			// get the center of the boss, and ring by ring performs a slam
			mokhaiotl.getNpc().animate(12414);
			shockArena(mokhaiotl.getNpc().getCentrePosition());
			event.delay(1);
			mokhaiotl.getNpc().animate(12415);
			if (delveLevel > 2) {
				event.delay(1);
				mokhaiotl.getNpc().animate(12414);
				shockArena(mokhaiotl.getNpc().getCentrePosition());
				event.delay(1);
				mokhaiotl.getNpc().animate(12415);
			}
			if (delveLevel > 4) {
				event.delay(1);
				mokhaiotl.getNpc().animate(12414);
				shockArena(mokhaiotl.getNpc().getCentrePosition());
				event.delay(1);
				mokhaiotl.getNpc().animate(12415);
			}
			if (delveLevel > 6) {
				event.delay(1);
				mokhaiotl.getNpc().animate(12414);
				shockArena(mokhaiotl.getNpc().getCentrePosition());
				event.delay(1);
				mokhaiotl.getNpc().animate(12415);
			}
		}).setCancelCondition(mokhaiotl::isDead);
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
		int SHOCKWAVE_RINGS = 16;
		for (int i = 0; i <= SHOCKWAVE_RINGS; i++) {
			var ringPositions = getRingPositions(centrePosition, i);
			for (var pos : ringPositions) {
				dangerTiles.add(pos);
				var tile = Tile.get(pos);
				if (tile != null && tile.gameObjects != null && !tile.gameObjects.isEmpty()) {
					var object = tile.gameObjects.getFirst();
					if (object.getId() == 57286)
						object.remove();
				}
			}
		}
		lightUpTiles(centrePosition.getRegion(), dangerTiles);
	}

	/**
	 * Applies graphical effects to tiles and damages players standing on dangerous tiles.
	 * For each given dangerous tile, a graphics effect is triggered, and players at that position
	 * are dealt fixed shockwave damage if they are not within a safe zone.
	 *
	 * @param region the region containing the tiles to be lit up
	 * @param dangerTiles a set of positions representing the tiles marked as dangerous
	 */
	private void lightUpTiles(Region region, HashSet<Position> dangerTiles) {
		dangerTiles.forEach(pos -> {
			World.sendGraphics(3405, 0, 0, pos);
			region.players.forEach(player -> {
				var earthShield = new AtomicReference<NPC>();
				if (Objects.nonNull(player.currentDynamicMap))
					player.currentDynamicMap.getNpcs().forEach(npc -> {
						if (npc.getId() == 14715)
							earthShield.set(npc);
					});
				// If the player is not within the bounds of the earthShield deal damage
				if (player.getPosition().isAtPosition(pos))
					if (earthShield.get() == null || !earthShield.get().getBounds().inBounds(player))
						player.hit(new Hit().randDamage(25, 50).ignoreDefence().ignorePrayer());
			});
		});
	}

	/**
	 * Generates a list of positions at a specified distance (ring) from the given center position.
	 *
	 * @param center the central position around which the ring is calculated
	 * @param ringDistance the distance from the center to the positions on the ring; must be non-negative
	 * @return a list of positions representing the ring around the center, or the center itself if the distance is zero
	 */
	private List<Position> getRingPositions(Position center, int ringDistance) {
		var positions = new ArrayList<Position>();
		if (ringDistance == 0) {
			positions.add(center);
			return positions;
		}
		int centerX = center.getX();
		int centerY = center.getY();
		int z = center.getZ();
		for (int x = centerX - ringDistance; x <= centerX + ringDistance; x++)
			for (int y = centerY - ringDistance; y <= centerY + ringDistance; y++)
				if (Math.abs(x - centerX) == ringDistance || Math.abs(y - centerY) == ringDistance)
					positions.add(new Position(x, y, z));
		return positions;
	}

}
