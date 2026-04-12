package io.ruin.model.entity.shared;

import io.ruin.model.entity.Entity;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;

public class Movement {

	public transient int readOffset, writeOffset;
	public transient int walkDirection = -1, runDirection = -1;
	public transient StepType stepType = StepType.NORMAL;
	protected transient int[] stepsX, stepsY;
	public transient byte[] steps_x = new byte[50];
	public transient byte[] steps_y = new byte[50];
	public transient StepType[] steps_type = new StepType[50];
	public transient int steps_len = 0;
	public transient int teleportStage = -1;
	public transient int teleportX, teleportY, teleportZ;

	public boolean isAtDestination() {
		return readOffset >= writeOffset;
	}

	public int getWalkDirection() {
		return walkDirection;
	}

	public int getRunDirection() {
		return runDirection;
	}

	public int[] getStepsX() {
		if (stepsX == null)
			stepsX = new int[50];
		return stepsX;
	}

	public int[] getStepsY() {
		if (stepsY == null)
			stepsY = new int[50];
		return stepsY;
	}

	/**
	 * Reset
	 */

	public void reset() {
		readOffset = 0;
		writeOffset = 0;
		stepType = StepType.NORMAL;
	}

	public void teleport(int x, int y) {
		teleport(x, y, 0);
	}

	public void teleport(int[] coords) {
		teleport(coords[0], coords[1], coords[2]);
	}

	public void teleport(Position position) {
		teleport(position.getX(), position.getY(), position.getZ());
	}

	public void teleport(Bounds bounds) {
		teleport(bounds.randomX(), bounds.randomY(), bounds.z);
	}

	public void teleport(int x, int y, int z) {
		teleportStage = 0;
		teleportX = x;
		teleportY = y;
		teleportZ = z;
	}

	public boolean finishTeleport(Position position) {
		if (teleportStage == 0) {
			teleportStage = 1;
			position.set(teleportX, teleportY, teleportZ);
			reset();
			return true;
		}
		if (teleportStage == 1)
			teleportStage = -1;
		return false;
	}

	public boolean isTeleportQueued() {
		return teleportStage == 0;
	}

	public boolean hasTeleportUpdate() {
		return teleportStage == 1;
	}

	public boolean teleporting() {
		return teleportStage != -1;
	}

	/**
	 * Misc
	 */

	public boolean hasMoved() {
		return walkDirection != -1 || runDirection != -1 || hasTeleportUpdate();
	}

	protected boolean step(Entity entity) {
		if (isAtDestination()) {
			return false;
		}

		int stepX = stepsX[readOffset];
		int stepY = stepsY[readOffset];

		Position position = entity.getPosition();
		int absX = position.getX();
		int absY = position.getY();
		int dx = stepX - absX;
		int dy = stepY - absY;

		if (dx < 0) {
			dx = -1;
		} else if (dx > 0) {
			dx = 1;
		}

		if (dy < 0) {
			dy = -1;
		} else if (dy > 0) {
			dy = 1;
		}

		int newX = absX + dx;
		int newY = absY + dy;

		var allowStep = entity.getRouteFinder().allowStep(newX, newY);
		if (allowStep != 0) {
			return false;
		}

		steps_x[steps_len] = (byte) dx;
		steps_y[steps_len] = (byte) dy;
		steps_type[steps_len] = stepType;
		steps_len += 1;

		position.set(newX, newY);
		if (newX == stepX && newY == stepY) {
			readOffset++;
		}
		return true;
	}

}
