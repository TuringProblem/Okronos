package io.ruin.model.map.object.actions.impl.deathoffice;

import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.map.object.actions.ObjectAction;

public class DeathDomain {

	public static void register() {
		ObjectAction.register(39547, "Enter", (player, obj) -> player.startEvent(event -> {
			player.lock();
			event.delay(2);
			player.dialogue(
				new MessageDialogue("You enter death's domain!")
			);
			player.getMovement().teleport(3171, 5726, 0);
			event.delay(1);
			player.unlock();
		}));
		ObjectAction.register(39549, "use", (player, obj) -> player.startEvent(event -> {
			player.lock();
			event.delay(2);
			player.dialogue(
				new MessageDialogue("You leave death's domain!")
			);
			player.getMovement().teleport(3100, 3486, 0);
			event.delay(1);
			player.unlock();
		}));
	}
}
