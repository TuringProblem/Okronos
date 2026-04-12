package io.ruin.model.activities.raids.toa.bosses.akkha;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.entity.shared.masks.EntityDirectionUpdate;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class WhiteOrb extends NPC {
	Bounds ORB_BOUNDS;
	private static final int ORB_RADIUS = 1;
	Direction direction;
	int type = -1;
	NPC boss;

	int x;
	int y;

	private double normalizedX;
	private double normalizedY;

	Player target = null;
	boolean passedPlayer = false;
	Position targetPlayerPos;

	public WhiteOrb(int id, int type, Position position, Bounds bounds, NPC boss) {
		super(id);
		this.spawn(position);
		this.x = Random.get(-1, 1);
		this.y = Random.get(-1, 1);
		this.boss = boss;
		if (x == 0 && y == 0) {
			x = 1;
			y = 1;
		}

		ORB_BOUNDS = bounds;
		target = Random.get(this.getPosition().getRegion().players);
		if (target != null) {
			targetPlayerPos = target.getPosition().copy();
			direction = Direction.getDirection(this.getPosition(), target.getPosition());
		}
		calculateDirection();

		if (type == 1) {
			moveOrb();
		} else {
			moveOrb();
		}
		World.startEvent(event -> {
			while (!this.isRemoved()) {
				event.delay(1);
				update();
			}
		});
	}

	private void update() {
		if (type == 1) {
			moveOrb();
		} else {
			moveOrb();
		}

		// Cache player positions to check once
		List<Player> playersToHit = new ArrayList<>();
		for (Player player : this.getPosition().getRegion().players) {
			if (player.getPosition().isWithinDistance(this.getPosition(), 0)) {
				playersToHit.add(player);
			}
		}

		// Process hits in batch
		for (Player player : playersToHit) {
			player.hit(new Hit().fixedDamage(25));
			Akkha akkha = (Akkha) boss.getCombat();
			akkha.damagedPlayer = true;
		}
	}

	// Optimize moveOrb() to avoid redundant calculations
	public void moveOrb() {
		Position currentPosition = this.getPosition();

		int nextX = currentPosition.getX() + x;
		int nextY = currentPosition.getY() + y;
		if (this.getPosition().getRegion().players.isEmpty()) {
			this.remove();
			return;
		}

		// Check clipping once
		if (hasClipping(nextX, nextY)) {
			handleWallCollision();
		} else {
			this.stepAbs(nextX, nextY, StepType.WALK);
		}
	}

	private boolean hasClipping(int x, int y) {
		return Tile.get(x, y, this.getPosition().getZ(), true).clipping != 0;
	}

	private void handleWallCollision() {
		if (this.x == 1)
			this.x = -1;
		else if (this.x == -1)
			this.x = 1;
		if (this.y == 1)
			this.y = -1;
		else if (this.y == -1)
			this.y = 1;
	}

	private Position getRandomBoundaryPosition() {
		int x, y;

		int direction = Random.get(1, 8);

		switch (direction) {
			case 1:
				// Horizontal side
				x = Random.get(ORB_BOUNDS.swX, ORB_BOUNDS.neX);
				y = ORB_BOUNDS.swY;
				break;
			case 2:
				// Horizontal side
				x = Random.get(ORB_BOUNDS.swX, ORB_BOUNDS.neX);
				y = ORB_BOUNDS.neY;
				break;
			case 3:
				// Vertical side
				x = ORB_BOUNDS.swX;
				y = Random.get(ORB_BOUNDS.swY, ORB_BOUNDS.neY);
				break;
			case 4:
				// Vertical side
				x = ORB_BOUNDS.neX;
				y = Random.get(ORB_BOUNDS.swY, ORB_BOUNDS.neY);
				break;
			case 5:
				// Diagonal - top-left to bottom-right
				x = Random.get(ORB_BOUNDS.swX, ORB_BOUNDS.neX);
				y = Random.get(ORB_BOUNDS.swY, ORB_BOUNDS.neY);
				break;
			case 6:
				// Diagonal - top-right to bottom-left
				x = Random.get(ORB_BOUNDS.swX, ORB_BOUNDS.neX);
				y = Random.get(ORB_BOUNDS.swY, ORB_BOUNDS.neY);
				break;
			case 7:
				// Diagonal - bottom-left to top-right
				x = Random.get(ORB_BOUNDS.swX, ORB_BOUNDS.neX);
				y = Random.get(ORB_BOUNDS.swY, ORB_BOUNDS.neY);
				break;
			case 8:
				// Diagonal - bottom-right to top-left
				x = Random.get(ORB_BOUNDS.swX, ORB_BOUNDS.neX);
				y = Random.get(ORB_BOUNDS.swY, ORB_BOUNDS.neY);
				break;
			default:
				x = ORB_BOUNDS.swX;
				y = ORB_BOUNDS.swY;
				break;
		}

		return new Position(x, y, ORB_BOUNDS.z);
	}

	public void moveOrbTowardsPlayer() {
		// Check if the target player is not set or if the orb has passed the player
		if (target == null) {
			target = Random.get(this.getPosition().getRegion().players);
			targetPlayerPos = target.getPosition().copy();
			passedPlayer = false;
		}

		// Move the orb towards the player's position
		if (!passedPlayer) {
			// Move towards the player's position until the orb passes the player
			this.stepAbs(targetPlayerPos.getX(), targetPlayerPos.getY(), StepType.WALK);

			// Check if the orb has passed the player
			if (this.getPosition().distance(targetPlayerPos) < 1) {
				passedPlayer = true;
			}
		} else {
			// Move the orb towards the player's location
			int nextX = (int) (this.getPosition().getX() + direction.deltaX);
			int nextY = (int) (this.getPosition().getY() + direction.deltaY);

			// Check for clipping at the next position
			if (hasClipping(nextX, nextY)) {
				// If clipping is detected, reset to pass through the player again
				passedPlayer = false;
				targetPlayerPos = target.getPosition().copy();
				direction = Direction.getDirection(this.getPosition(), target.getPosition());
			} else {
				// Move towards the player's location
				this.stepAbs(nextX, nextY, StepType.WALK);
			}
		}
	}

	private void handleWallCollisionForPlayerOrb() {
		int x = this.getPosition().getX();
		int y = this.getPosition().getY();

		if (!ORB_BOUNDS.inBounds(x, y, this.getPosition().getZ(), ORB_RADIUS)) {
			int newX = Math.max(ORB_BOUNDS.swX + ORB_RADIUS, Math.min(x, ORB_BOUNDS.neX - ORB_RADIUS));
			int newY = Math.max(ORB_BOUNDS.swY + ORB_RADIUS, Math.min(y, ORB_BOUNDS.neY - ORB_RADIUS));
			this.stepAbs(newX, newY, StepType.RUN);
			if (Tile.get(x, y, this.getPosition().getZ(), true).clipping != 0) {
				this.remove();
			}
		}
	}

	private void calculateDirection() {
		if (target == null)
			target = Random.get(this.getPosition().getRegion().players);
		// Calculate the direction towards the player
		int deltaX = target.getPosition().getX() - this.getPosition().getX();
		int deltaY = target.getPosition().getY() - this.getPosition().getY();

		// Normalize the direction vector
		double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		normalizedX = deltaX / distance;
		normalizedY = deltaY / distance;
	}

}
