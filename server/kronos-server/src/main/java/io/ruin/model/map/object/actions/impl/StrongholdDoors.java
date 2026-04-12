package io.ruin.model.map.object.actions.impl;

import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.map.object.actions.impl.dungeons.StrongholdSecurity;

public class StrongholdDoors {
	public static void interactWithDoor(Player player, GameObject door) {
		player.face(door);
		Position playerLocation = player.getPosition();
		Direction playerFacingDirection = Direction.getDirection(player.getPosition(), door.getPosition());


		if (playerFacingDirection == Direction.NORTH) { // Facing north
			player.startEvent(event -> {
				//player animation here
				player.lock();
				event.delay(1);
				player.getMovement().teleport(playerLocation.getX(), playerLocation.getY() + 1);
				player.unlock();
			});
		} else if (playerFacingDirection == Direction.EAST) { // Facing east
			player.startEvent(event -> {
				//player animation here
				player.lock();
				event.delay(1);
				player.getMovement().teleport(playerLocation.getX() + 1, playerLocation.getY());
				player.unlock();
			});
		} else if (playerFacingDirection == Direction.SOUTH) { // Facing south
			player.startEvent(event -> {
				//player animation here
				player.lock();
				event.delay(1);
				player.getMovement().teleport(playerLocation.getX(), playerLocation.getY() - 1);
				player.unlock();
			});
		} else if (playerFacingDirection == Direction.WEST) { // Facing west
			player.startEvent(event -> {
				//player animation here
				player.lock();
				event.delay(1);
				player.getMovement().teleport(playerLocation.getX() - 1, playerLocation.getY());
				player.unlock();
			});
		}
	}

	public static void register() {
		ObjectAction.register(19206, "open", StrongholdDoors::interact);
		ObjectAction.register(19207, "open", StrongholdDoors::interact);
		ObjectAction.register(17100, "open", StrongholdDoors::interact);
		ObjectAction.register(17009, "open", StrongholdDoors::interact);
	}

	private static void interact(Player player, GameObject obj) {
		boolean atObjX = obj.x == player.getAbsX();
		boolean atObjY = obj.y == player.getAbsY();

		if (obj.getDirection() == 0 && atObjX)
			StrongholdSecurity.teleportPlayer(player, player.getAbsX() - 1, player.getAbsY());
		else if (obj.getDirection() == 1 && atObjY)
			StrongholdSecurity.teleportPlayer(player, obj.x, obj.y + 1);
		else if (obj.getDirection() == 2 && atObjX)
			StrongholdSecurity.teleportPlayer(player, obj.x + 1, obj.y);
		else if (obj.getDirection() == 3 && atObjY)
			StrongholdSecurity.teleportPlayer(player, obj.x, obj.y - 1);
		else
			StrongholdSecurity.teleportPlayer(player, obj.x, obj.y);
	}

}
