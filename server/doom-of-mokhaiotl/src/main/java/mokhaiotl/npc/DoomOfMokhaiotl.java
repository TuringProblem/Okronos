package mokhaiotl.npc;

import core.task.Continuation;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import lombok.Getter;
import lombok.Setter;
import mokhaiotl.area.MokhaiotlArena;
import mokhaiotl.combat.MokhaiotlCombat;
import mokhaiotl.combat.attacks.impl.BurrowedSlamAttack;

import java.util.Objects;

import static core.task.api.API.fork;
import static core.task.api.API.sleep;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-28
 */
public class DoomOfMokhaiotl extends NPC {

	protected final MokhaiotlArena mokhaiotlArena;

	@Getter @Setter
	protected int delveLevel = 1;
	private Position zoomDestination = null;
	private Player trackedTarget = null;

	public DoomOfMokhaiotl(MokhaiotlArena mokhaiotlArena) {
		super(14707);
		this.mokhaiotlArena = mokhaiotlArena;
		getDef().occupyTiles = false;
	}

	public void transformDoom(int newId) {
		transform(newId);
		getDef().occupyTiles = false;
	}

	private int getTrampleDamage() {
		return switch (delveLevel) {
			case 1, 2, 3, 4, 5 -> 10;
			case 6 -> 20;
			case 7 -> 30;
			default -> 40;
		};
	}

	@Override
	public void process() {
		super.process();
		if (zoomDestination != null) {
			// Trample the player
			if (getBounds().inBounds(trackedTarget))
				trackedTarget.hit(new Hit(HitType.DAMAGE).fixedDamage(getTrampleDamage()));

			// if we've moved in this process tick, destroy rocks if we move over them
			var gameObjects = getCentrePosition().getTile().gameObjects;
			// check if the center is running over a rock
			if (gameObjects != null && !gameObjects.isEmpty()) {
				if (gameObjects.getFirst().getId() == 57286)
					gameObjects.getFirst().remove();
			}
		}
	}

	public Continuation.Void burrowDigToTarget(Player target, MokhaiotlCombat mokhaiotl) {
		return () -> {
			this.trackedTarget = target;
			// get the destination position
			zoomDestination = getZoomDestination(mokhaiotl);
			// send the eye graphic
			World.sendGraphics(3415, 0, 0, zoomDestination);
			mokhaiotl.reset();
			mokhaiotl.getNpc().face(zoomDestination);

			fork(() -> {
				while (zoomDestination != null) {
					sleep(3);
					if (Objects.nonNull(zoomDestination))
						World.sendGraphics(3415, 0, 0, zoomDestination);
				}
			});

			sleep(3);

			var parkingPosition = zoomDestination.translated(-2, -2);
			mokhaiotl.getNpc().stepAbs(parkingPosition.getX(), parkingPosition.getY(), StepType.RUN);

			// wait till we've reached the destination
			while (!mokhaiotl.isDead()) {
				sleep(1);
				if (getPosition().isAtPosition(parkingPosition)) break;
			}
			World.sendGraphics(3416, 0, 0, zoomDestination);
			zoomDestination = null;
			sleep(3);
			face(target);
			if (target.get("MOKHAIOTL_DELVE_LEVEL", 1) > 5)
				new BurrowedSlamAttack().invoke(target, getCombat());
			((MokhaiotlCombat) getCombat()).resetBurrowed();
		};
	}

	/**
	 * Calculates the zoom destination position in the combat arena based on the player's position
	 * and direction from the boss. Ensures the destination is within the arena boundaries.
	 *
	 * @param mokhaiotl The combat instance representing the boss, used for position and arena bounds.
	 * @return The calculated {@link Position} representing the zoom destination.
	 */
	private Position getZoomDestination(MokhaiotlCombat mokhaiotl) {
		var playerDirection = getDirectionOfPlayerFromBoss(trackedTarget, mokhaiotl);
		var playerPosition = trackedTarget.getPosition();
		var position = Position.of(
			playerPosition.getX() + (playerDirection.deltaX * 4),
			playerPosition.getY() + (playerDirection.deltaY * 4),
			playerPosition.getZ()
		);
		var arena = mokhaiotlArena.getArenaBounds();
		if (!arena.inBounds(position)) {
			int clampedX = Math.max(arena.swX, Math.min(arena.neX, position.getX()));
			int clampedY = Math.max(arena.swY, Math.min(arena.neY, position.getY()));

			if (playerDirection.deltaX > 0 && clampedX == arena.neX)
				clampedX = arena.neX - 1;
			else if (playerDirection.deltaX < 0 && clampedX == arena.swX)
				clampedX = arena.swX + 1;

			if (playerDirection.deltaY > 0 && clampedY == arena.neY)
				clampedY = arena.neY - 1;
			else if (playerDirection.deltaY < 0 && clampedY == arena.swY)
				clampedY = arena.swY + 1;

			return Position.of(clampedX, clampedY, position.getZ());
		}
		return position;
	}

	/**
	 * Determines the direction from the boss's center position to the player's position.
	 *
	 * @param target The player whose position is used in determining the direction.
	 * @param mokhaiotl The NPCCombat instance representing the boss, used to calculate the boss's center position.
	 * @return The {@link Direction} from the boss to the player.
	 */
	private Direction getDirectionOfPlayerFromBoss(Player target, NPCCombat mokhaiotl) {
		var playerPosition = target.getPosition();
		var bossBounds = mokhaiotl.getNpc().getBounds();

		var bossX = bossBounds.swX + 2;
		var bossY = bossBounds.swY + 2;

		var deltaX = playerPosition.getX() - bossX;
		var deltaY = playerPosition.getY() - bossY;

		return Direction.getDirection(deltaX, deltaY);
	}

}
