package io.ruin.model.entity.npc.actions;

import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;

//NPC that sells Neitiznot helm to players (meant for ironmen)
public class MawnisBurowgar {

	public static void register() {
		NPCAction.register(1880, "talk-to", (player, npc) -> {
			player.dialogue(
				new NPCDialogue(1880, "Hello adventurer. I have the ability to sell you a Helm of" +
					" Neitiznot for 36,438 coins. Would you like to purchase one?"),
				new OptionsDialogue("Purchase a Helm?",
					new Option("Yes", () -> purchaseHelm(player)),
					new Option("Nevermind"))
			);
		});
	}

	public static void startDialogue(Player player) {
		player.dialogue(
			new NPCDialogue(1878, "Hello adventurer. I have the ability to sell you a Helm of" +
				" Neitiznot for 36,438 coins. Would you like to purchase one?"),
			new OptionsDialogue("Purchase a Helm?",
				new Option("Yes", () -> purchaseHelm(player)),
				new Option("Nevermind"))
		);
	}

	public static void purchaseHelm(Player player) {
		if (!player.getInventory().contains(995, 36438)) {
			player.dialogue(new MessageDialogue("You do not have enough coins to purchase this item."));
		} else {
			player.getInventory().remove(995, 36438);
			player.getInventory().addOrDrop(10828, 1);
			player.sendMessage("You purchase a Helm of Neitiznot.");
		}
	}
}
