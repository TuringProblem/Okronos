package io.ruin.model.map.object.actions.impl.prifddinas.towerofvoices;

import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.map.object.actions.ObjectAction;

public class GrandLibraryTeleporterOut {

	public static void register() {
		ObjectAction.register(36615, 1, (player, obj) -> player.startEvent(event -> {
			player.lock();
			player.getPacketSender().fadeOut();
			event.delay(1);
			player.dialogue(
				new MessageDialogue("Welcome to the Tower of Voices.")
			);
			player.getMovement().teleport(3257, 6082, 2);
			player.getPacketSender().fadeIn();
			event.delay(1);
			player.unlock();
		}));
	}

}
