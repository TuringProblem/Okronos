package io.ruin.model.activities.blastfurnace;

import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.ActionDialogue;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.stat.StatType;

public class Foreman {

	private static final int foreman = 2923;

	public static void register() {
		NPCAction.register(foreman, 1, (player, npc) -> startDialogue(player));
	}

	public static void startDialogue(Player player) {
		player.dialogue(
			new NPCDialogue(foreman, "Hello, I can help you operate the blast furnace" +
				" without the 60 smithing requirement..." +
				"...for a small fee of course."),
			new OptionsDialogue(
				new Option("I'd like to use the blast furnace please.", () -> useFurnace(player)),
				new Option("I'd like to understand how it works.", () ->
					new ActionDialogue(() -> new OptionsDialogue(
						new Option("I'd like to know about how to start.", () -> howToStart(player)),
						new Option("I'd like to know the ore calculations.", () -> oreCalculations(player)),
						new Option("Nevermind"))
					))
			)
		);
	}

	public static void howToStart(Player player) {
		player.dialogue(
			new NPCDialogue(foreman, "Put your ore on the conveyor belt to get it smelted." +
				" We'll stoke the boiler and operate the rest for you."),
			new NPCDialogue(foreman, "You have to help repair the drive belt when it breaks, though." +
				" Just hit it with a hammer - that usually works."),
			new NPCDialogue(foreman, "You'll have to put money in the coffer to pay for our services." +
				" We charge 72,000 coins per hour; money is deducted as long as you're in this room with us.")
		);
	}

	public static void oreCalculations(Player player) {
		player.dialogue(
			new NPCDialogue(foreman, "The ore to coal rates are as follows: Steel: 1:1    Mithril: 1:2    Adamantite: 1:3    Runite: 1:4")
		);
	}

	public static void useFurnace(Player player) {
		player.dialogue(
			new PlayerDialogue("I'd like to use the blast furnace please."),
			new OptionsDialogue(
				new Option("15 minutes for 25,000 coins.", () -> payForeman(player)),
				new Option("Nevermind")
			)
		);
	}

	public static void payForeman(Player player) {
		if (player.getStats().get(StatType.Smithing).fixedLevel < 60) {
			if (player.getInventory().contains(995, 25000)) {
				player.getInventory().remove(995, 25000);
				player.dialogue(new MessageDialogue("You pay 25,000 coins to the foreman for 15 minutes of access."));
			} else {
				player.sendMessage("You need at least 25,000 coins to participate without 60 smithing!");
			}
		} else if (player.getStats().get(StatType.Smithing).fixedLevel >= 60) {
			player.dialogue(
				new NPCDialogue(foreman, "You don't need to pay me anything! You already have enough experience.")
			);
		}
	}
}
