package io.ruin.model.map.object.actions.impl.slepe;

import io.ruin.model.map.object.actions.ObjectAction;

public class NightmareArea {
	/* can be renamed at a later date if need be */

	public static void register() {
		ObjectAction.register(32637, 1, (player, obj) -> player.startEvent(event -> {
			player.getMovement().teleport(3738, 9703, 1);
			player.getPacketSender().fadeIn();
			event.delay(1);
			player.unlock();
		}));
	}
}
