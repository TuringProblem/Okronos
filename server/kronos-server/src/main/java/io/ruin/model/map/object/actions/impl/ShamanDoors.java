package io.ruin.model.map.object.actions.impl;

import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;

public class ShamanDoors {

	public static void interactWithDoor(Player player, GameObject door) {
		player.face(door);
		Position playerLocation = player.getPosition();
		Direction playerFacingDirection = Direction.getDirection(player.getPosition(), door.getPosition());

		switch (door.getId()) {
			case 34642:
				if (playerFacingDirection == Direction.NORTH) { // Facing north
					player.startEvent(event -> {
						//player animation here
						player.lock();
						event.delay(1);
						player.getMovement().teleport(playerLocation.getX(), playerLocation.getY() + 2);
						player.unlock();
					});
				} else if (playerFacingDirection == Direction.SOUTH) { // Facing south
					player.startEvent(event -> {
						//player animation here
						player.lock();
						event.delay(1);
						player.getMovement().teleport(playerLocation.getX(), playerLocation.getY() - 2);
						player.unlock();
					});
				} else if (playerFacingDirection == Direction.SOUTH_WEST) { // Facing south
					player.startEvent(event -> {
						//player animation here
						player.lock();
						event.delay(1);
						player.getMovement().teleport(playerLocation.getX(), playerLocation.getY() - 2);
						player.unlock();
					});
				} else if (playerFacingDirection == Direction.NORTH_WEST) { // Facing south
					player.startEvent(event -> {
						//player animation here
						player.lock();
						event.delay(1);
						player.getMovement().teleport(playerLocation.getX(), playerLocation.getY() + 2);
						player.unlock();
					});
				}
				break;
			case 34643:
			case 34644:
			case 34645:
			case 34646:
				if (playerFacingDirection == Direction.EAST) { // Facing east
					player.startEvent(event -> {
						//player animation here
						player.lock();
						event.delay(1);
						player.getMovement().teleport(playerLocation.getX() + 2, playerLocation.getY());
						player.unlock();
					});
				} else if (playerFacingDirection == Direction.SOUTH_EAST) { // Facing east
					player.startEvent(event -> {
						//player animation here
						player.lock();
						event.delay(1);
						player.getMovement().teleport(playerLocation.getX() + 2, playerLocation.getY());
						player.unlock();
					});
				} else if (playerFacingDirection == Direction.WEST) { // Facing west
					player.startEvent(event -> {
						//player animation here
						player.lock();
						event.delay(1);
						player.getMovement().teleport(playerLocation.getX() - 2, playerLocation.getY());
						player.unlock();
					});
				} else if (playerFacingDirection == Direction.SOUTH_WEST) { // Facing west
					player.startEvent(event -> {
						//player animation here
						player.lock();
						event.delay(1);
						player.getMovement().teleport(playerLocation.getX() - 2, playerLocation.getY());
						player.unlock();
					});
				}
				break;
		}


		if (playerFacingDirection == Direction.NORTH) { // Facing north
			player.startEvent(event -> {
				//player animation here
				player.lock();
				event.delay(1);
				player.getMovement().teleport(playerLocation.getX(), playerLocation.getY() + 2);
				player.unlock();
			});
		} else if (playerFacingDirection == Direction.EAST) { // Facing east
			player.startEvent(event -> {
				//player animation here
				player.lock();
				event.delay(1);
				player.getMovement().teleport(playerLocation.getX() + 2, playerLocation.getY());
				player.unlock();
			});
		} else if (playerFacingDirection == Direction.SOUTH) { // Facing south
			player.startEvent(event -> {
				//player animation here
				player.lock();
				event.delay(1);
				player.getMovement().teleport(playerLocation.getX(), playerLocation.getY() - 2);
				player.unlock();
			});
		} else if (playerFacingDirection == Direction.WEST) { // Facing west
			player.startEvent(event -> {
				//player animation here
				player.lock();
				event.delay(1);
				player.getMovement().teleport(playerLocation.getX() - 2, playerLocation.getY());
				player.unlock();
			});
		}
	}

	public static void register() {
		ObjectAction.register(34642, 1, ShamanDoors::interactWithDoor);
		ObjectAction.register(34643, 1, ShamanDoors::interactWithDoor);
		ObjectAction.register(34644, 1, ShamanDoors::interactWithDoor);
		ObjectAction.register(34645, 1, ShamanDoors::interactWithDoor);
		ObjectAction.register(34646, 1, ShamanDoors::interactWithDoor);
	}
}
