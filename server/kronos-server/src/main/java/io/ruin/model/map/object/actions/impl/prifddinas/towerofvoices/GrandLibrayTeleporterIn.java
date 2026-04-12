package io.ruin.model.map.object.actions.impl.prifddinas.towerofvoices;

import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.map.object.actions.ObjectAction;

public class GrandLibrayTeleporterIn {

	public static void register() {
		ObjectAction.register(36614, 1, (player, obj) -> player.startEvent(event -> {
			player.lock();
			player.getPacketSender().fadeOut();
			event.delay(1);
			player.dialogue(
				new MessageDialogue("Welcome to the Grand Library.")
			);
			player.getMovement().teleport(3232, 12534, 0);
			player.getPacketSender().fadeIn();
			event.delay(1);
			player.unlock();
		}));
	}

}
