package io.ruin.model.entity.npc.actions;

import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.shop.ShopManager;

public class MonkOfEntrana {
	public static void register() {
		NPCAction.register(1165, "talk-to", (player, npc) ->
			new OptionsDialogue(
				new Option("Yes, please take me there!", () -> {
					player.getMovement().startTeleport(-1, event -> {
						player.animate(2140);
						event.delay(1);
						event.delay(1);
						player.animate(714);
						player.graphics(111, 110, 0);
						event.delay(3);
						player.sendMessage("You board the ship and travel to Entrana.");
						player.getMovement().teleport(2834, 3335, 0);
					});
				}),
				new Option("Nevermind"
				)));
		NPCAction.register(1165, "take-boat", (player, npc) -> {
			player.getMovement().startTeleport(-1, event -> {
				player.animate(2140);
				event.delay(1);
				event.delay(1);
				player.animate(714);
				player.graphics(111, 110, 0);
				event.delay(3);
				player.sendMessage("You board the ship and travel to Entrana.");
				player.getMovement().teleport(2834, 3335, 0);
			});
		});
		NPCAction.register(1166, "talk-to", (player, npc) ->
			new OptionsDialogue(
				new Option("Yes, please take me there!", () -> {
					player.getMovement().startTeleport(-1, event -> {
						player.animate(2140);
						event.delay(1);
						event.delay(1);
						player.animate(714);
						player.graphics(111, 110, 0);
						event.delay(3);
						player.sendMessage("You board the ship and travel to Entrana.");
						player.getMovement().teleport(2834, 3335, 0);
					});
				}),
				new Option("Nevermind"
				)));
		NPCAction.register(1166, "take-boat", (player, npc) -> {
			player.getMovement().startTeleport(-1, event -> {
				player.animate(2140);
				event.delay(1);
				event.delay(1);
				player.animate(714);
				player.graphics(111, 110, 0);
				event.delay(3);
				player.sendMessage("You board the ship and travel to Entrana.");
				player.getMovement().teleport(2834, 3335, 0);
			});
		});
		NPCAction.register(1167, "talk-to", (player, npc) ->
			new OptionsDialogue(
				new Option("Yes, please take me there!", () -> {
					player.getMovement().startTeleport(-1, event -> {
						player.animate(2140);
						event.delay(1);
						event.delay(1);
						player.animate(714);
						player.graphics(111, 110, 0);
						event.delay(3);
						player.sendMessage("You board the ship and travel to Entrana.");
						player.getMovement().teleport(2834, 3335, 0);
					});
				}),
				new Option("Nevermind"
				)));
		NPCAction.register(1167, "take-boat", (player, npc) -> {
			player.getMovement().startTeleport(-1, event -> {
				player.animate(2140);
				event.delay(1);
				event.delay(1);
				player.animate(714);
				player.graphics(111, 110, 0);
				event.delay(3);
				player.sendMessage("You board the ship and travel to Entrana.");
				player.getMovement().teleport(2834, 3335, 0);
			});
		});

		NPCAction.register(1168, "take-boat", (player, npc) -> {
			player.getMovement().startTeleport(-1, event -> {
				player.animate(2140);
				event.delay(1);
				event.delay(1);
				player.animate(714);
				player.graphics(111, 110, 0);
				event.delay(3);
				player.sendMessage("You board the ship and travel to Port Sarim.");
				player.getMovement().teleport(3048, 3234, 0);
			});
		});
		NPCAction.register(1169, "take-boat", (player, npc) -> {
			player.getMovement().startTeleport(-1, event -> {
				player.animate(2140);
				event.delay(1);
				event.delay(1);
				player.animate(714);
				player.graphics(111, 110, 0);
				event.delay(3);
				player.sendMessage("You board the ship and travel to Port Sarim.");
				player.getMovement().teleport(3048, 3234, 0);
			});
		});
		NPCAction.register(1170, "take-boat", (player, npc) -> {
			player.getMovement().startTeleport(-1, event -> {
				player.animate(2140);
				event.delay(1);
				event.delay(1);
				player.animate(714);
				player.graphics(111, 110, 0);
				event.delay(3);
				player.sendMessage("You board the ship and travel to Port Sarim.");
				player.getMovement().teleport(3048, 3234, 0);
			});
		});
	}
}
