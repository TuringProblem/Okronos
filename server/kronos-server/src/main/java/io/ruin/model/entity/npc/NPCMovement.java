package io.ruin.model.entity.npc;

import java.util.ArrayList;

import io.ruin.api.utils.Random;
import io.ruin.model.entity.shared.Movement;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.utility.Utils;

public class NPCMovement extends Movement {

	private NPC npc;

	public NPCMovement(NPC npc) {
		this.npc = npc;
	}

	/**
	 * Processing
	 */
	public void process() {
		// Update position first
		npc.updateLastPosition();
		steps_len = 0;

		this.walkDirection = -1;
		this.runDirection = -1;
		if (finishTeleport(npc.getPosition())) {
			npc.getPosition().getTile().checkTriggers(npc);
		} else {
			if (!step(npc)) {
				// Only do random walking if not currently stepping
				if (npc.getDef() != null && !npc.isRemoved()) {
					randomWalk();
				}
				return;
			}

			boolean forceRun = stepType == StepType.RUN;
			boolean ran = forceRun && step(npc);
			var dx = npc.getPosition().x() - npc.getLastPosition().x();
			var dy = npc.getPosition().y() - npc.getLastPosition().y();

			if (ran) {
				runDirection = getDirection(dx, dy);
				if (runDirection == -1)
					walkDirection = getDirection(dx, dy);
			} else {
				walkDirection = getDirection(dx, dy);
			}

			npc.getPosition().getTile().checkTriggers(npc);
		}

		// Update region and multi-combat status
		npc.updateRegion();
		npc.checkMulti();
		Tile.occupy(npc);
	}

	private void randomWalk() {
		// Skip if conditions aren't met
		if (!npc.isRandomWalkAllowed()) {
			return;
		}

		// Only roll for movement sometimes (keeps the 1-in-4 chance)
		if (!Random.rollDie(32, 1)) {
			return;
		}

		// Additional checks for combat
		NPCCombat combat = npc.getCombat();
		if (combat != null && (combat.isDead() || combat.getTarget() != null)) {
			return;
		}

		// If we get here, do the random walk
		int x = npc.walkBounds.randomX();
		int y = npc.walkBounds.randomY();
		npc.getRouteFinder().routeAbsolute(x, y);
	}

	/**
	 * NPC - ForceMovement
	 */

	public void force(int diffX1, int diffY1, int diffX2, int diffY2, int speed1, int speed2, Direction faceDirection) {
		Position pos = npc.getPosition();
		teleport(pos.getX() + (diffX1 + diffX2), pos.getY() + (diffY1 + diffY2), pos.getZ());
		npc.forceMovementUpdate.set(-diffX1, -diffY1, -diffX2, -diffY2, speed1, speed2, faceDirection.clientValue);
	}

	/**
	 * Misc
	 */

	private static int getDirection(int dx, int dy) {
		if (dy == 1) {
			if (dx == -1)
				return 0;
			if (dx == 0)
				return 1;
			if (dx == 1)
				return 2;
		} else if (dy == 0) {
			if (dx == -1)
				return 3;
			if (dx == 1)
				return 4;
		} else if (dy == -1) {
			if (dx == -1)
				return 5;
			if (dx == 0)
				return 6;
			if (dx == 1)
				return 7;
		}
		return -1;
	}

}
