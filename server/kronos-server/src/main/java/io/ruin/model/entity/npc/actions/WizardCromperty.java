package io.ruin.model.entity.npc.actions;

import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;

public class WizardCromperty {

	public static void register() {
		NPCAction.register(8480, "talk-to", (player, npc) -> {
			player.getMovement().startTeleport(-1, event -> {
				player.animate(2140);
				event.delay(1);
				event.delay(1);
				player.animate(714);
				player.graphics(111, 110, 0);
				event.delay(3);
				player.sendMessage("The wizard teleports you to the essence mine.");
				player.getMovement().teleport(2910, 4830, 0);
			});
		});
		NPCAction.register(8480, "teleport", (player, npc) -> {
			player.getMovement().startTeleport(-1, event -> {
				player.animate(2140);
				event.delay(1);
				event.delay(1);
				player.animate(714);
				player.graphics(111, 110, 0);
				event.delay(3);
				player.sendMessage("The wizard teleports you to the essence mine.");
				player.getMovement().teleport(2910, 4830, 0);
			});
		});
		NPCAction.register(8481, "talk-to", (player, npc) -> {
			player.getMovement().startTeleport(-1, event -> {
				player.animate(2140);
				event.delay(1);
				event.delay(1);
				player.animate(714);
				player.graphics(111, 110, 0);
				event.delay(3);
				player.sendMessage("The wizard teleports you to the essence mine.");
				player.getMovement().teleport(2910, 4830, 0);
			});
		});
		NPCAction.register(4913, "talk-to", (player, npc) -> {
			player.getMovement().startTeleport(-1, event -> {
				player.animate(2140);
				event.delay(1);
				event.delay(1);
				player.animate(714);
				player.graphics(111, 110, 0);
				event.delay(3);
				player.sendMessage("Brimstail teleports you to the essence mine.");
				player.getMovement().teleport(2910, 4830, 0);
			});
		});
		NPCAction.register(4913, "teleport", (player, npc) -> {
			player.getMovement().startTeleport(-1, event -> {
				player.animate(2140);
				event.delay(1);
				event.delay(1);
				player.animate(714);
				player.graphics(111, 110, 0);
				event.delay(3);
				player.sendMessage("Brimstail teleports you to the essence mine.");
				player.getMovement().teleport(2910, 4830, 0);
			});
		});
	}

}
