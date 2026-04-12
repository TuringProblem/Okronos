package io.ruin.model.item.actions.impl;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.actions.ItemAction;

public class PerkCrystal {

	public static void activateCrystal(Player player) {
		player.dialogue(new OptionsDialogue("What do you need?",
			new Option("What perk set do I have active?", () -> {
				String dialogue = player.getPlayerPerkHandler().getActivePerkSets(player).size() > 0 ? "You currently have the perk set '" + player.getPlayerPerkHandler().getActivePerkSets(player).get(0).perkSet.getPerkSetName() + "' active." : "You don't have a perk set active, check out the perk combinations in the repository.";
				player.dialogue(new NPCDialogue(7958, dialogue));
			}),
			new Option("What is my current perk task?", () -> {
				String dialogue = player.currentPerkTask != null ? "Your current perk task is to " + player.currentPerkTask.description.toLowerCase() + player.perkTaskCurrentAmount + player.currentPerkTask.description_2 + "." : "You don't have a perk task active, talk to me to get a new one.";
				player.dialogue(new NPCDialogue(7958, dialogue));
			}),
			new Option("Nevermind.", player::closeDialogue)
		));
	}

	public static void register() {
		ItemAction.registerInventory(30496, "interact", (player, item) -> activateCrystal(player));
		ItemAction.registerInventory(30496, "check", (player, item) -> {
			String dialogue = player.currentPerkTask != null ? "Your current perk task is to " + player.currentPerkTask.description.toLowerCase() + player.perkTaskCurrentAmount + player.currentPerkTask.description_2 + "." : "You don't have a perk task active, talk to the perk master to get a new one.";
			player.sendMessage(dialogue);
		});
	}

}
