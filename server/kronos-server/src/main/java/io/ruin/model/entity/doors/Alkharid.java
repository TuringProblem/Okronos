package io.ruin.model.entity.doors;

import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;

public class Alkharid {
	public static void interactWithDoor(Player player, GameObject door) {
		player.face(door);
		Position playerLocation = player.getPosition();
		Direction playerFacingDirection = Direction.getDirection(player.getPosition(), door.getPosition());

		if (playerFacingDirection == Direction.EAST) { // Facing east
			player.startEvent(event -> {
				//player animation here
				player.lock();
				event.delay(1);
				player.getMovement().teleport(playerLocation.getX() + 1, playerLocation.getY());
				player.unlock();
			});
		} else { // Facing west
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
		ObjectAction.register(44598, 1, Alkharid::interactWithDoor);
		ObjectAction.register(44599, 1, Alkharid::interactWithDoor);
	}
}
