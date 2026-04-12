package io.ruin.model.map.object.actions.impl;

import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.map.object.actions.ObjectAction;

public class KaramjaBoats {

	public static void register() {
		ObjectAction.register(2081, "cross", (player, object) ->
			player.dialogue(
				new OptionsDialogue(
					new Option("Travel to Port Sarim", () -> travelToPortSarim(player)),
					new Option("Nevermind")
				)
			));
		ObjectAction.register(2083, "cross", (player, object) ->
			player.dialogue(
				new OptionsDialogue(
					new Option("Travel to Karamja", () -> travelToKaramjaFromPort(player)),
					new Option("Nevermind")
				)
			));
		ObjectAction.register(2085, "cross", (player, object) ->
			player.dialogue(
				new OptionsDialogue(
					new Option("Travel to Brimhaven", () -> travelToBrimhaven(player)),
					new Option("Nevermind")
				)
			));
		ObjectAction.register(2087, "cross", (player, object) ->
			player.dialogue(
				new OptionsDialogue(
					new Option("Travel to Ardougne", () -> travelArdougne(player)),
					new Option("Travel to Rimmington", () -> travelToRimmington(player)),
					new Option("Nevermind")
				)
			));
		NPCAction.register(5364, "talk-to", (player, object) ->
			player.dialogue(
				new OptionsDialogue(
					new Option("Travel to Port Khazard", () -> travelToPortKhazard(player)),
					new Option("Nevermind")
				)
			));
		ObjectAction.register(17402, "cross", (player, object) ->
			player.dialogue(
				new OptionsDialogue(
					new Option("Travel to Cairn Isle", () -> travelToCairnIsle(player)),
					new Option("Nevermind")
				)
			));
	}

	public static void travelArdougne(Player player) {
		player.getMovement().startTeleport(event -> {
			player.getPacketSender().fadeOut();
			event.delay(1);
			player.getMovement().teleport(2683, 3271, 0);
			player.getPacketSender().clearFade();
			player.dialogue(new MessageDialogue("You board the ship, and arrive in Ardougne."));
		});
	}

	public static void travelToRimmington(Player player) {
		player.getMovement().startTeleport(event -> {
			player.getPacketSender().fadeOut();
			event.delay(1);
			player.getMovement().teleport(2915, 3225, 0);
			player.getPacketSender().clearFade();
			player.dialogue(new MessageDialogue("You board the ship, and arrive in Rimmington."));
		});
	}

	public static void travelToPortSarim(Player player) {
		player.getMovement().startTeleport(event -> {
			player.getPacketSender().fadeOut();
			event.delay(1);
			player.getMovement().teleport(3029, 3217, 0);
			player.getPacketSender().clearFade();
			player.dialogue(new MessageDialogue("You board the ship, and arrive in Port Sarim."));
		});
	}

	public static void travelToKaramjaFromPort(Player player) {
		player.getMovement().startTeleport(event -> {
			player.getPacketSender().fadeOut();
			event.delay(1);
			player.getMovement().teleport(2956, 3146, 0);
			player.getPacketSender().clearFade();
			player.dialogue(new MessageDialogue("You board the ship, and arrive in Karamja."));
		});
	}

	public static void travelToBrimhaven(Player player) {
		player.getMovement().startTeleport(event -> {
			player.getPacketSender().fadeOut();
			event.delay(1);
			player.getMovement().teleport(2772, 3234, 0);
			player.getPacketSender().clearFade();
			player.dialogue(new MessageDialogue("You board the ship, and arrive in Brimhaven."));
		});
	}

	public static void travelToPortKhazard(Player player) {
		player.getMovement().startTeleport(event -> {
			player.getPacketSender().fadeOut();
			event.delay(1);
			player.getMovement().teleport(2674, 3144, 0);
			player.getPacketSender().clearFade();
			player.dialogue(new MessageDialogue("You board the ship, and arrive in Port Khazard."));
		});
	}

	public static void travelToCairnIsle(Player player) {
		player.getMovement().startTeleport(event -> {
			player.getPacketSender().fadeOut();
			event.delay(1);
			player.getMovement().teleport(2763, 2956, 1);
			player.getPacketSender().clearFade();
			player.dialogue(new MessageDialogue("You board the ship, and arrive in Cairn Isle."));
		});
	}

}
