package io.ruin.model.entity.npc.actions;

import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;

public class RugMerchant {

	public static void register() {
		NPCAction.register(17, "talk-to", (player, npc) -> {
			talkTo(player);
		});
		NPCAction.register(17, "travel", (player, npc) -> {
			askTravel(player);
		});
		NPCAction.register(22, "talk-to", (player, npc) -> {
			talkTo(player);
		});
		NPCAction.register(22, "travel", (player, npc) -> {
			askTravel(player);
		});
	}

	public static void talkTo(Player player) {
		player.dialogue(
			new NPCDialogue(17, "Hello traveler. Would you like to travel somewhere?"),
			new OptionsDialogue(
				new Option("Pollnivneach", () -> flyToPollnivneach(player)),
				new Option("Nardah", () -> flyToNardah(player)),
				new Option("Shantay Pass", () -> flyToShantay(player)),
				new Option("Nevermind")
			)
		);
	}

	public static void askTravel(Player player) {
		player.dialogue(
			new OptionsDialogue(
				new Option("Pollnivneach", () -> flyToPollnivneach(player)),
				new Option("Nardah", () -> flyToNardah(player)),
				new Option("Shantay Pass", () -> flyToShantay(player)),
				new Option("Nevermind")
			)
		);
	}

	public static void flyToPollnivneach(Player player) {
		player.getMovement().startTeleport(-1, event -> {
			player.animate(2140);
			event.delay(1);
			event.delay(1);
			player.animate(714);
			player.graphics(111, 110, 0);
			event.delay(3);
			player.sendMessage("You travel to Pollnivneach.");
			player.getMovement().teleport(3348, 2944, 0);
		});
	}

	;


	public static void flyToNardah(Player player) {
		player.getMovement().startTeleport(-1, event -> {
			player.animate(2140);
			event.delay(1);
			event.delay(1);
			player.animate(714);
			player.graphics(111, 110, 0);
			event.delay(3);
			player.sendMessage("You travel to Nardah.");
			player.getMovement().teleport(3399, 2916, 0);
		});
	}

	;

	public static void flyToShantay(Player player) {
		player.getMovement().startTeleport(-1, event -> {
			player.animate(2140);
			event.delay(1);
			event.delay(1);
			player.animate(714);
			player.graphics(111, 110, 0);
			event.delay(3);
			player.sendMessage("You travel to Shantay Pass.");
			player.getMovement().teleport(3308, 3108, 0);
		});
	}

	;


}
