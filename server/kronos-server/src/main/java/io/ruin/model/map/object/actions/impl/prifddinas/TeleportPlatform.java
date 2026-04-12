package io.ruin.model.map.object.actions.impl.prifddinas;

import io.ruin.model.map.object.actions.ObjectAction;

public class TeleportPlatform {

	public static void register() {
		ObjectAction.register(36490, 1, (player, obj) -> {
			if (player.getHeight() == 0) {
				player.lock();
				player.getPacketSender().fadeOut();
				player.getMovement().teleport(player.getAbsX(), player.getAbsY(), 2);
				player.getPacketSender().fadeIn();
				player.unlock();
			} else if (player.getHeight() == 2) {
				player.lock();
				player.getPacketSender().fadeOut();
				player.getMovement().teleport(player.getAbsX(), player.getAbsY(), 0);
				player.getPacketSender().fadeIn();
				player.unlock();
			}
		});
	}

}
