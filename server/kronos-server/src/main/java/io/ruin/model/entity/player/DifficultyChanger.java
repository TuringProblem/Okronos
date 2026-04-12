package io.ruin.model.entity.player;

import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.stat.StatType;

public class DifficultyChanger {
	public static final int PARTY_PETE = 5792;
	public static final int MAX_DIFFICULTY_CHANGES = 3;

	public static void HandleDifficultyChangeDialogue(Player player) {
		if (player.getDifficultyChangeCount() >= MAX_DIFFICULTY_CHANGES) {
			player.dialogue(new NPCDialogue(PARTY_PETE, "You are unable to change your game mode on this account as you have reached the maximum number of changes."));
			return;
		}

		player.dialogue(new NPCDialogue(PARTY_PETE, "What would you like your new difficulty mode to be?"),
			new OptionsDialogue("Choose your difficulty mode",
				new Option("I'd like to play on Easy mode.", () -> ConfirmDifficultyChange(player, Difficulty.EASY)),
				new Option("I'd like to play on Intermediate mode.", () -> ConfirmDifficultyChange(player, Difficulty.INTERMEDIATE)),
				new Option("I'd like to play on Hard mode.", () -> ConfirmDifficultyChange(player, Difficulty.HARD)),
				new Option("I'd like to play on Extreme mode.", () -> ConfirmDifficultyChange(player, Difficulty.EXTREME)),
				new Option("Nevermind.", player::closeDialogue)
			)
		);
	}

	public static void ConfirmDifficultyChange(Player player, Difficulty mode) {
		player.startEvent(e -> {
			player.dialogue(new NPCDialogue(PARTY_PETE,
				"WARNING: Changing your difficulty will reset your stats back to level 1. You are also only able to change your difficulty " + (MAX_DIFFICULTY_CHANGES - player.getDifficultyChangeCount()) + " more time(s) so please be certain this is the mode you wish to play on."));
			e.waitForDialogue(player);
			player.dialogue(new NPCDialogue(PARTY_PETE, "You've chosen to change to " + mode.toString().toLowerCase() + " difficulty mode, is this correct?"),
				new OptionsDialogue("You have " + (MAX_DIFFICULTY_CHANGES - player.getDifficultyChangeCount()) + " more difficulty change(s)",
					new Option("Yes I'd like to play on " + mode.toString().toLowerCase() + " difficulty mode.", () -> {
						HandleDifficultyChange(player, mode);
					}),
					new Option("No I'd like to choose again.", () -> {
						HandleDifficultyChangeDialogue(player);
					}),
					new Option("Nevermind.", player::closeDialogue)));
		});
	}

	public static void HandleDifficultyChange(Player player, Difficulty mode) {
		player.startEvent(event -> {
			if (player.getDifficultyChangeCount() >= MAX_DIFFICULTY_CHANGES) {
				player.dialogue(new NPCDialogue(PARTY_PETE, "You are unable to change your game mode on this account as you have reached the maximum number of changes."));
				return;
			}

			if (player.getDifficulty() == mode) {
				player.dialogue(new NPCDialogue(PARTY_PETE, "You're already playing on that difficulty mode!"));
				event.waitForDialogue(player);
				HandleDifficultyChangeDialogue(player);
				return;
			}

			if (!player.getEquipment().isEmpty()) {
				player.dialogue(new NPCDialogue(PARTY_PETE, "You must remove all your equipment before changing your difficulty mode!"));
				return;
			}
			if (player.getInventory().getFreeSlots() < 4 && mode == Difficulty.EXTREME) {
				player.dialogue(new NPCDialogue(PARTY_PETE, "You don't have enough inventory space to accept the items given to extreme players!"));
				return;
			}
			if (player.getInventory().getFreeSlots() < 1 && mode == Difficulty.HARD) {
				player.dialogue(new NPCDialogue(PARTY_PETE, "You don't have enough inventory space to accept the items given to hard players!"));
				return;
			}

			player.setDifficulty(mode);
			player.incrementDifficultyChanges();
			if (mode == Difficulty.EXTREME) {
				boolean hasOne = player.getInventory().contains(30478) || player.getBank().contains(30478);
				boolean hasTwo = player.getInventory().contains(30479) || player.getBank().contains(30479);
				boolean hasThree = player.getInventory().contains(30480) || player.getBank().contains(30480);
				Item trainingStaff = new Item(30480);
				AttributeExtensions.addCharges(trainingStaff, 10000);
				Item trainingBow = new Item(30478);
				AttributeExtensions.addCharges(trainingBow, 10000);
				Item trainingSword = new Item(30479);
				AttributeExtensions.addCharges(trainingSword, 10000);
				if (!hasThree)
					player.getInventory().add(trainingStaff);
				if (!hasTwo)
					player.getInventory().add(trainingSword);
				if (!hasOne)
					player.getInventory().add(trainingBow);
			} else if (mode == Difficulty.HARD) {
				for (int i = 30478; i < 30481; i++) {
					boolean hasTrainingItemInInv = player.getInventory().contains(i);
					boolean hasTrainingItemInBank = player.getBank().contains(i);
					if (hasTrainingItemInInv)
						player.getInventory().remove(i, player.getInventory().getAmount(i));
					if (hasTrainingItemInBank)
						player.getBank().remove(i, player.getBank().getAmount(i));
				}
			} else {
				for (Item item : player.getBank().getItems()) {
					if (item == null) continue;
					for (int i = 30478; i < 30481; i++) {
						if (item.getId() == i)
							player.getBank().remove(item);
					}
				}
				for (Item item : player.getInventory().getItems()) {
					if (item == null) continue;
					for (int i = 30478; i < 30481; i++) {
						if (item.getId() == i)
							player.getInventory().remove(item);
					}
				}
			}

			player.getPrayer().deactivateAll();
			for (int i = 0; i < 22; i++) {
				player.getStats().set(StatType.get(i), 1, 0);
			}
			player.getStats().set(StatType.Hitpoints, 10, 1154);
			player.getStats().set(StatType.Construction, 1, 0);
			player.dialogue(new NPCDialogue(PARTY_PETE, "All done, You are now playing on " + player.getDifficulty().toString().toLowerCase() + " difficulty mode!"));
		});
	}

	public static void HandleInteraction(Player player) {
		player.dialogue(
			new NPCDialogue(PARTY_PETE, "Would you like me to change your difficulty mode for you?"),
			new OptionsDialogue(new Option("Yes, I'd like to change my difficulty mode.", () -> {
				HandleDifficultyChangeDialogue(player);
			}), new Option("No I'd like to stay the same difficulty mode.", player::closeDialogue))
		);
	}

	public static void register() {
		NPCAction.register(PARTY_PETE, 1, (player, npc) -> HandleInteraction(player));
	}
}
