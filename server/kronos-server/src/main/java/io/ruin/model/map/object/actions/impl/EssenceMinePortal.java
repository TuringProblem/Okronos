package io.ruin.model.map.object.actions.impl;

import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.actions.ObjectAction;

public class EssenceMinePortal {

	public static void register() {
		ObjectAction.register(34825, 1, (player, obj) -> climb(player, 3253, 3401, 0));
	}

	private static void climb(Player player, int x, int y, int z) {
		player.resetActions(true, true, true);
		player.lock(); //keep lock outside of event!
		player.startEvent(e -> {
			player.getPacketSender().fadeOut();
			e.delay(1);
			player.getMovement().teleport(new Position(x, y, z));
			player.getPacketSender().clearFade();
			player.getPacketSender().fadeIn();
			player.unlock();
		});
	}

}
