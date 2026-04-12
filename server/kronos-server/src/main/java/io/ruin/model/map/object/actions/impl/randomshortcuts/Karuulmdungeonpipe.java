package io.ruin.model.map.object.actions.impl.randomshortcuts;

import io.ruin.model.map.object.actions.ObjectAction;

public class Karuulmdungeonpipe {

	public static void register() {
		ObjectAction.register(34655, 1, (player, obj) -> {
			if (player.getPosition().equals(1316, 10213, 0)) {
				player.lock();
				player.getPacketSender().fadeOut();
				player.getMovement().teleport(1346, 10231, 0);
				player.getPacketSender().fadeIn();
				player.unlock();
			} else if (player.getPosition().equals(1346, 10232, 0)) {
				player.lock();
				player.getPacketSender().fadeOut();
				player.getMovement().teleport(1316, 10213, 0);
				player.getPacketSender().fadeIn();
				player.unlock();
			}
		});
	}

}
